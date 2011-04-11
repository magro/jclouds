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
package org.jclouds.vcloud.config;

import static com.google.common.base.Throwables.propagate;
import static org.jclouds.Constants.PROPERTY_SESSION_INTERVAL;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.http.RequiresHttp;
import org.jclouds.rest.AsyncClientFactory;
import org.jclouds.rest.ConfiguresRestClient;
import org.jclouds.rest.suppliers.MemoizedRetryOnTimeOutButNotOnAuthorizationExceptionSupplier;
import org.jclouds.vcloud.VCloudExpressAsyncClient;
import org.jclouds.vcloud.VCloudExpressClient;
import org.jclouds.vcloud.VCloudExpressLoginAsyncClient;
import org.jclouds.vcloud.domain.CatalogItem;
import org.jclouds.vcloud.domain.VCloudExpressVAppTemplate;
import org.jclouds.vcloud.domain.VCloudSession;
import org.jclouds.vcloud.functions.VCloudExpressVAppTemplatesForCatalogItems;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;


/**
 * Configures the VCloud authentication service connection, including logging
 * and http transport.
 * 
 * @author Adrian Cole
 */
@RequiresHttp
@ConfiguresRestClient
public abstract class BaseVCloudExpressRestClientModule<S extends VCloudExpressClient, A extends VCloudExpressAsyncClient>
      extends CommonVCloudRestClientModule<S, A> {

   public BaseVCloudExpressRestClientModule(Class<S> syncClientType, Class<A> asyncClientType) {
      super(syncClientType, asyncClientType);
   }

   @Override
   protected void configure() {
      bind(new TypeLiteral<Function<Iterable<? extends CatalogItem>, Iterable<? extends VCloudExpressVAppTemplate>>>() {
      }).to(new TypeLiteral<VCloudExpressVAppTemplatesForCatalogItems>() {
      });
      super.configure();
   }

   @Provides
   @Singleton
   protected VCloudExpressLoginAsyncClient provideVCloudLogin(AsyncClientFactory factory) {
      return factory.create(VCloudExpressLoginAsyncClient.class);
   }

   @Provides
   @Singleton
   protected Supplier<VCloudSession> provideVCloudTokenCache(@Named(PROPERTY_SESSION_INTERVAL) long seconds,
         final VCloudExpressLoginAsyncClient login) {
      return new MemoizedRetryOnTimeOutButNotOnAuthorizationExceptionSupplier<VCloudSession>(authException, seconds,
            new Supplier<VCloudSession>() {

               @Override
               public VCloudSession get() {
                  try {
                     return login.login().get(10, TimeUnit.SECONDS);
                  } catch (Exception e) {
                     propagate(e);
                     assert false : e;
                     return null;
                  }
               }

            });
   }

}
