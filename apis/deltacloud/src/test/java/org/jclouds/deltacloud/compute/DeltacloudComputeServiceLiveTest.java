/**
 *
 * Copyright (C) 2011 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package org.jclouds.deltacloud.compute;

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.jclouds.compute.BaseComputeServiceLiveTest;
import org.jclouds.compute.ComputeServiceContextFactory;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.deltacloud.DeltacloudAsyncClient;
import org.jclouds.deltacloud.DeltacloudClient;
import org.jclouds.domain.LocationScope;
import org.jclouds.rest.RestContext;
import org.jclouds.ssh.jsch.config.JschSshClientModule;
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author Adrian Cole
 */
@Test(groups = "live", enabled = true, sequential = true)
public class DeltacloudComputeServiceLiveTest extends BaseComputeServiceLiveTest {
   public DeltacloudComputeServiceLiveTest() {
      provider = "deltacloud";
   }

   @Override
   protected JschSshClientModule getSshModule() {
      return new JschSshClientModule();
   }

   public void testAssignability() throws Exception {
      @SuppressWarnings("unused")
      RestContext<DeltacloudClient, DeltacloudAsyncClient> tmContext = new ComputeServiceContextFactory()
               .createContext(provider, identity, credential).getProviderSpecificContext();
   }

   @Override
   protected void checkNodes(Iterable<? extends NodeMetadata> nodes, String tag) throws IOException {
      super.checkNodes(nodes, tag);
      for (NodeMetadata node : nodes) {
         assertEquals(node.getLocation().getScope(), LocationScope.ZONE);
      }
   }

   @Test(enabled = true, dependsOnMethods = "testReboot", expectedExceptions = UnsupportedOperationException.class)
   public void testSuspendResume() throws Exception {
      super.testSuspendResume();
   }

   @Test(enabled = true, dependsOnMethods = "testSuspendResume")
   @Override
   public void testGetNodesWithDetails() throws Exception {
      super.testGetNodesWithDetails();
   }

   @Test(enabled = true, dependsOnMethods = "testSuspendResume")
   @Override
   public void testListNodes() throws Exception {
      super.testListNodes();
   }

   @Test(enabled = true, dependsOnMethods = { "testListNodes", "testGetNodesWithDetails" })
   @Override
   public void testDestroyNodes() {
      super.testDestroyNodes();
   }
}
