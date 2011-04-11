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
package org.jclouds.rest.config;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.concurrent.internal.SyncProxy;
import org.jclouds.internal.ClassMethodArgs;

import com.google.common.base.Throwables;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

/**
 * 
 * @author Adrian Cole
 */
@Singleton
public class ClientProvider<S, A> implements Provider<S> {
   @Inject
   Injector injector;
   private final Class<S> syncClientType;
   private final Class<A> asyncClientType;
   private final Map<Class<?>, Class<?>> sync2Async;

   @Inject
   ClientProvider(Class<S> syncClientType, Class<A> asyncClientType,
            Map<Class<?>, Class<?>> sync2Async) {
      this.asyncClientType = asyncClientType;
      this.syncClientType = syncClientType;
      this.sync2Async = sync2Async;
   }

   @Override
   @Singleton
   public S get() {
      A client = (A) injector.getInstance(Key.get(asyncClientType));
      ConcurrentMap<ClassMethodArgs, Object> delegateMap = injector.getInstance(Key.get(
               new TypeLiteral<ConcurrentMap<ClassMethodArgs, Object>>() {
               }, Names.named("sync")));
      try {
         return (S) SyncProxy.proxy(syncClientType, new SyncProxy(syncClientType, client,
                  delegateMap, sync2Async));
      } catch (Exception e) {
         Throwables.propagate(e);
         assert false : "should have propagated";
         return null;
      }
   }
}