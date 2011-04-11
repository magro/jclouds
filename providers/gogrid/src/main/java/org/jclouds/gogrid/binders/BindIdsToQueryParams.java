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
package org.jclouds.gogrid.binders;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.jclouds.gogrid.reference.GoGridQueryParams.ID_KEY;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.ws.rs.core.UriBuilder;

import org.jclouds.http.HttpRequest;
import org.jclouds.http.utils.ModifyRequest;
import org.jclouds.rest.Binder;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Longs;

/**
 * Binds IDs to corresponding query parameters
 * 
 * @author Oleksiy Yarmula
 */
@Singleton
public class BindIdsToQueryParams implements Binder {
   private final Provider<UriBuilder> builder;

   @Inject
   BindIdsToQueryParams(Provider<UriBuilder> builder) {
      this.builder = builder;
   }

   /**
    * Binds the ids to query parameters. The pattern, as specified by GoGrid's specification, is:
    * 
    * https://api.gogrid.com/api/grid/server/get ?id=5153 &id=3232
    * 
    * @param request
    *           request where the query params will be set
    * @param input
    *           array of String params
    */
   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {

      if (checkNotNull(input, "input is null") instanceof Long[]) {
         Long[] names = (Long[]) input;
         request = ModifyRequest.addQueryParam(request, ID_KEY, ImmutableList.copyOf(names), builder.get());
      } else if (input instanceof long[]) {
         long[] names = (long[]) input;
         request = ModifyRequest.addQueryParam(request, ID_KEY, Longs.asList(names), builder.get());
      } else {
         throw new IllegalArgumentException("this binder is only valid for Long[] arguments: "
                  + input.getClass());
      }
      return request;
   }
}
