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
package org.jclouds.vcloud.functions;

import static org.jclouds.concurrent.FutureIterables.transformParallel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.Constants;
import org.jclouds.logging.Logger;
import org.jclouds.vcloud.CommonVCloudAsyncClient;
import org.jclouds.vcloud.domain.Org;

import com.google.common.base.Function;

/**
 * @author Adrian Cole
 */
@Singleton
public class OrgsForNames implements Function<Iterable<String>, Iterable<? extends Org>> {
   @Resource
   public Logger logger = Logger.NULL;
   private final CommonVCloudAsyncClient aclient;
   private final ExecutorService executor;

   @Inject
   OrgsForNames(CommonVCloudAsyncClient aclient, @Named(Constants.PROPERTY_USER_THREADS) ExecutorService executor) {
      this.aclient = aclient;
      this.executor = executor;
   }

   @Override
   public Iterable<? extends Org> apply(Iterable<String> from) {
      return transformParallel(from, new Function<String, Future<Org>>() {

         @SuppressWarnings("unchecked")
         @Override
         public Future<Org> apply(String from) {
            return (Future<Org>) aclient.findOrgNamed(from);
         }

      }, executor, null, logger, "organizations for names");
   }

}