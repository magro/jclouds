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
package org.jclouds.savvis.vpdc.filters;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.ws.rs.core.HttpHeaders;

import org.jclouds.http.HttpException;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpRequestFilter;
import org.jclouds.http.utils.ModifyRequest;
import org.jclouds.savvis.vpdc.internal.VCloudToken;

/**
 * Adds the VCloud Token to the request as a cookie
 * 
 * @author Adrian Cole
 * 
 */
@Singleton
public class SetVCloudTokenCookie implements HttpRequestFilter {
   private Provider<String> vcloudTokenProvider;

   @Inject
   public SetVCloudTokenCookie(@VCloudToken Provider<String> authTokenProvider) {
      this.vcloudTokenProvider = authTokenProvider;
   }

   @Override
   public HttpRequest filter(HttpRequest request) throws HttpException {
      return ModifyRequest.replaceHeader(request, HttpHeaders.COOKIE, "vcloud-token=" + vcloudTokenProvider.get());
   }

}