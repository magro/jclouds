/**
 *
 * Copyright (C) 2009 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 */
package org.jclouds.vcloud.terremark.compute;

import static com.google.common.base.Preconditions.checkArgument;
import static org.jclouds.vcloud.terremark.options.AddInternetServiceOptions.Builder.withDescription;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.SortedSet;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.jclouds.compute.Image;
import org.jclouds.logging.Logger;
import org.jclouds.vcloud.domain.Task;
import org.jclouds.vcloud.domain.VApp;
import org.jclouds.vcloud.domain.VAppStatus;
import org.jclouds.vcloud.terremark.TerremarkVCloudClient;
import org.jclouds.vcloud.terremark.domain.InternetService;
import org.jclouds.vcloud.terremark.domain.Node;
import org.jclouds.vcloud.terremark.domain.PublicIpAddress;
import org.jclouds.vcloud.terremark.domain.TerremarkVApp;
import org.jclouds.vcloud.terremark.options.TerremarkInstantiateVAppTemplateOptions;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

/**
 * 
 * @author Adrian Cole
 */
public class TerremarkVCloudComputeClient {
   @Resource
   protected Logger logger = Logger.NULL;

   private final Predicate<URI> taskTester;
   private final TerremarkVCloudClient tmClient;

   @Inject
   public TerremarkVCloudComputeClient(TerremarkVCloudClient tmClient, Predicate<URI> successTester) {
      this.tmClient = tmClient;
      this.taskTester = successTester;
   }

   private Map<Image, String> imageCatalogIdMap = ImmutableMap.<Image, String> builder().put(
            Image.CENTOS_53, "6").put(Image.RHEL_53, "8").put(Image.UMBUNTU_90, "10").put(
            Image.UMBUNTU_JEOS, "11").build();

   public String start(String name, int minCores, int minMegs, Image image) {
      checkArgument(imageCatalogIdMap.containsKey(image), "image not configured: " + image);
      String templateId = imageCatalogIdMap.get(image);

      logger.debug(">> instantiating vApp name(%s) minCores(%d) minMegs(%d) template(%s)", name,
               minCores, minMegs, templateId);
      TerremarkVApp vApp = tmClient.instantiateVAppTemplate(name, templateId,
               TerremarkInstantiateVAppTemplateOptions.Builder.cpuCount(minCores)
                        .megabytes(minMegs));
      logger.debug("<< instantiated VApp(%s)", vApp.getId());

      logger.debug(">> deploying vApp(%s)", vApp.getId());
      vApp = blockUntilVAppStatusOrThrowException(vApp, tmClient.deployVApp(vApp.getId()),
               "deploy", VAppStatus.OFF);
      logger.debug("<< deployed vApp(%s)", vApp.getId());

      logger.debug(">> powering vApp(%s)", vApp.getId());
      vApp = blockUntilVAppStatusOrThrowException(vApp, tmClient.powerOnVApp(vApp.getId()),
               "powerOn", VAppStatus.ON);
      logger.debug("<< on vApp(%s)", vApp.getId());

      return vApp.getId();
   }

   /**
    * 
    * @throws ElementNotFoundException
    *            if no address is configured
    */
   public InetAddress getAnyPrivateAddress(String id) {
      TerremarkVApp vApp = tmClient.getVApp(id);
      return Iterables.getLast(vApp.getNetworkToAddresses().values());
   }

   public void reboot(String id) {
      TerremarkVApp vApp = tmClient.getVApp(id);
      logger.debug(">> rebooting vApp(%s)", vApp.getId());
      blockUntilVAppStatusOrThrowException(vApp, tmClient.resetVApp(vApp.getId()), "reset",
               VAppStatus.ON);
      logger.debug("<< on vApp(%s)", vApp.getId());
   }

   public InetAddress createPublicAddressMappedToPorts(VApp vApp, int... ports) {
      PublicIpAddress sshIp = null;
      InetAddress privateAddress = Iterables.getLast(vApp.getNetworkToAddresses().values());
      for (int port : ports) {
         InternetService is = null;
         if (sshIp == null) {
            logger.debug(">> creating InternetService %d", port);
            is = tmClient.addInternetService(vApp.getName() + "-" + port, "TCP", port,
                     withDescription(String.format("port %d access to serverId: %s name: %s", port,
                              vApp.getId(), vApp.getName())));
            sshIp = is.getPublicIpAddress();
         } else {
            logger.debug(">> adding InternetService %s:%d", sshIp.getAddress().getHostAddress(),
                     port);
            is = tmClient.addInternetServiceToExistingIp(sshIp.getId() + "", vApp.getName() + "-"
                     + port, "TCP", port,
                     withDescription(String.format("port %d access to serverId: %s name: %s", port,
                              vApp.getId(), vApp.getName())));
         }
         logger.debug("<< created InternetService(%s) %s:%d", is.getId(), is.getPublicIpAddress()
                  .getAddress().getHostAddress(), is.getPort());
         logger.debug(">> adding Node %s:%d -> %s:%d", is.getPublicIpAddress().getAddress()
                  .getHostAddress(), is.getPort(), privateAddress.getHostAddress(), port);
         Node node = tmClient
                  .addNode(is.getId(), privateAddress, vApp.getName() + "-" + port, port);
         logger.debug("<< added Node(%s)", node.getId());
      }
      return sshIp.getAddress();
   }

   public void stop(String id) {
      TerremarkVApp vApp = tmClient.getVApp(id);

      SERVICE: for (InternetService service : tmClient.getAllInternetServices()) {
         for (Node node : tmClient.getNodes(service.getId())) {
            if (vApp.getNetworkToAddresses().containsValue(node.getIpAddress())) {
               logger.debug(">> deleting Node(%s) %s:%d -> %s:%d", node.getId(), service
                        .getPublicIpAddress().getAddress().getHostAddress(), service.getPort(),
                        node.getIpAddress().getHostAddress(), node.getPort());
               tmClient.deleteNode(node.getId());
               logger.debug("<< deleted Node(%s)", node.getId());
               SortedSet<Node> nodes = tmClient.getNodes(service.getId());
               if (nodes.size() == 0) {
                  logger.debug(">> deleting InternetService(%s) %s:%d", service.getId(), service
                           .getPublicIpAddress().getAddress().getHostAddress(), service.getPort());
                  tmClient.deleteInternetService(service.getId());
                  logger.debug("<< deleted InternetService(%s)", service.getId());
                  continue SERVICE;
               }
            }
         }
      }

      if (vApp.getStatus() != VAppStatus.OFF) {
         logger.debug(">> powering off vApp(%s)", vApp.getId());
         blockUntilVAppStatusOrThrowException(vApp, tmClient.powerOffVApp(vApp.getId()),
                  "powerOff", VAppStatus.OFF);
         logger.debug("<< off vApp(%s)", vApp.getId());
      }
      logger.debug(">> deleting vApp(%s)", vApp.getId());
      tmClient.deleteVApp(id);
      logger.debug("<< deleted vApp(%s)", vApp.getId());
   }

   private TerremarkVApp blockUntilVAppStatusOrThrowException(TerremarkVApp vApp, Task deployTask,
            String taskType, VAppStatus expectedStatus) {
      if (!taskTester.apply(deployTask.getLocation())) {
         throw new TaskException(taskType, vApp, deployTask);
      }

      vApp = tmClient.getVApp(vApp.getId());
      if (vApp.getStatus() != expectedStatus) {
         throw new VAppException(String.format("vApp %s status %s should be %s after %s", vApp
                  .getId(), vApp.getStatus(), expectedStatus, taskType), vApp);
      }
      return vApp;
   }

   public static class TaskException extends VAppException {

      private final Task task;
      /** The serialVersionUID */
      private static final long serialVersionUID = 251801929573211256L;

      public TaskException(String type, TerremarkVApp vApp, Task task) {
         super(String.format("failed to %s vApp %s status %s;task %s status %s", type,
                  vApp.getId(), vApp.getStatus(), task.getLocation(), task.getStatus()), vApp);
         this.task = task;
      }

      public Task getTask() {
         return task;
      }

   }

   public static class SocketNotOpenException extends RuntimeException {

      private final InetSocketAddress socket;
      /** The serialVersionUID */
      private static final long serialVersionUID = 251801929573211256L;

      public SocketNotOpenException(InetSocketAddress socket) {
         super("socket not open: " + socket);
         this.socket = socket;
      }

      public InetSocketAddress getSocket() {
         return socket;
      }

   }

   public static class VAppException extends RuntimeException {

      private final TerremarkVApp vApp;
      /** The serialVersionUID */
      private static final long serialVersionUID = 251801929573211256L;

      public VAppException(String message, TerremarkVApp vApp) {
         super(message);
         this.vApp = vApp;
      }

      public TerremarkVApp getvApp() {
         return vApp;
      }

   }
}