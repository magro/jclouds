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

import java.net.URI;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.vcloud.domain.ReferenceType;
import org.jclouds.vcloud.endpoints.Org;

import com.google.common.base.Function;
import com.google.common.base.Supplier;

/**
 * 
 * @author Adrian Cole
 */
@Singleton
public class OrgNameToEndpoint implements Function<Object, URI> {
   private final Supplier<Map<String, ReferenceType>> orgNameToEndpointSupplier;
   private final URI defaultUri;

   @Inject
   public OrgNameToEndpoint(@Org Supplier<Map<String, ReferenceType>> orgNameToEndpointSupplier, @Org URI defaultUri) {
      this.orgNameToEndpointSupplier = orgNameToEndpointSupplier;
      this.defaultUri = defaultUri;
   }

   public URI apply(Object from) {
      try {
         Map<String, ReferenceType> orgNameToEndpoint = orgNameToEndpointSupplier.get();
         return from == null ? defaultUri : orgNameToEndpoint.get(from).getHref();
      } catch (NullPointerException e) {
         throw new NoSuchElementException("org " + from + " not found in " + orgNameToEndpointSupplier.get().keySet());
      }
   }

}