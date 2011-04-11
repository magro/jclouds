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
package org.jclouds.ibmdev.compute.strategy;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.strategy.DestroyNodeStrategy;
import org.jclouds.compute.strategy.GetNodeMetadataStrategy;
import org.jclouds.ibmdev.IBMDeveloperCloudClient;

/**
 * @author Adrian Cole
 */
@Singleton
public class IBMDeveloperCloudDestroyNodeStrategy implements DestroyNodeStrategy {
   private final IBMDeveloperCloudClient client;
   private final GetNodeMetadataStrategy getNode;

   @Inject
   protected IBMDeveloperCloudDestroyNodeStrategy(IBMDeveloperCloudClient client, GetNodeMetadataStrategy getNode) {
      this.client = checkNotNull(client, "client");
      this.getNode = checkNotNull(getNode, "getNode");
   }

   @Override
   public NodeMetadata destroyNode(String id) {
      client.deleteInstance(id);
      return getNode.getNode(id);
   }
}