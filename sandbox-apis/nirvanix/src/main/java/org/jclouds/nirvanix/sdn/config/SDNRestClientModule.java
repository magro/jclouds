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
package org.jclouds.nirvanix.sdn.config;

import org.jclouds.blobstore.config.BlobStoreObjectModule;
import org.jclouds.http.RequiresHttp;
import org.jclouds.nirvanix.sdn.SDNAsyncClient;
import org.jclouds.nirvanix.sdn.SDNClient;
import org.jclouds.rest.config.RestClientModule;

/**
 * Configures the SDN authentication service connection, including logging and http transport.
 * 
 * @author Adrian Cole
 */
@RequiresHttp
public class SDNRestClientModule extends RestClientModule<SDNClient, SDNAsyncClient> {

   public SDNRestClientModule() {
      super(SDNClient.class, SDNAsyncClient.class);
   }

   @Override
   protected void configure() {
      install(new BlobStoreObjectModule());
      super.configure();
   }

   @Override
   protected void bindRetryHandlers() {
      // TODO retry on 401 by AuthenticateRequest.update()
   }

}