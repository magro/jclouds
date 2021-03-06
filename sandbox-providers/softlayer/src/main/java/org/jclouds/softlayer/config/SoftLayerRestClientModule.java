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

package org.jclouds.softlayer.config;

import java.util.Map;

import org.jclouds.http.HttpErrorHandler;
import org.jclouds.http.HttpRetryHandler;
import org.jclouds.http.RequiresHttp;
import org.jclouds.http.annotation.ClientError;
import org.jclouds.http.annotation.Redirection;
import org.jclouds.http.annotation.ServerError;
import org.jclouds.http.handlers.BackoffLimitedRetryHandler;
import org.jclouds.json.config.GsonModule.DateAdapter;
import org.jclouds.json.config.GsonModule.Iso8601DateAdapter;
import org.jclouds.rest.ConfiguresRestClient;
import org.jclouds.rest.config.RestClientModule;
import org.jclouds.softlayer.SoftLayerAsyncClient;
import org.jclouds.softlayer.SoftLayerClient;
import org.jclouds.softlayer.features.DatacenterAsyncClient;
import org.jclouds.softlayer.features.DatacenterClient;
import org.jclouds.softlayer.features.VirtualGuestAsyncClient;
import org.jclouds.softlayer.features.VirtualGuestClient;
import org.jclouds.softlayer.handlers.SoftLayerErrorHandler;

import com.google.common.collect.ImmutableMap;

/**
 * Configures the SoftLayer connection.
 * 
 * @author Adrian Cole
 */
@RequiresHttp
@ConfiguresRestClient
public class SoftLayerRestClientModule extends RestClientModule<SoftLayerClient, SoftLayerAsyncClient> {

   public static final Map<Class<?>, Class<?>> DELEGATE_MAP = ImmutableMap.<Class<?>, Class<?>> builder()//
         .put(VirtualGuestClient.class, VirtualGuestAsyncClient.class)//
         .put(DatacenterClient.class, DatacenterAsyncClient.class)//
         .build();

   public SoftLayerRestClientModule() {
      super(SoftLayerClient.class, SoftLayerAsyncClient.class, DELEGATE_MAP);
   }

   @Override
   protected void configure() {
      bind(DateAdapter.class).to(Iso8601DateAdapter.class);
      super.configure();
   }

   @Override
   protected void bindErrorHandlers() {
      bind(HttpErrorHandler.class).annotatedWith(Redirection.class).to(SoftLayerErrorHandler.class);
      bind(HttpErrorHandler.class).annotatedWith(ClientError.class).to(SoftLayerErrorHandler.class);
      bind(HttpErrorHandler.class).annotatedWith(ServerError.class).to(SoftLayerErrorHandler.class);
   }

   @Override
   protected void bindRetryHandlers() {
      bind(HttpRetryHandler.class).annotatedWith(ClientError.class).to(BackoffLimitedRetryHandler.class);
   }

}
