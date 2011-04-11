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
package org.jclouds.nirvanix.sdn.binders;

import static org.easymock.classextension.EasyMock.createMock;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.net.URI;

import javax.inject.Provider;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.UriBuilder;

import org.jclouds.http.HttpRequest;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;
import com.sun.jersey.api.uri.UriBuilderImpl;

/**
 * Tests behavior of {@code BindMetadataToQueryParams}
 * 
 * @author Adrian Cole
 */
@Test(groups = "unit", testName = "sdn.BindMetadataToQueryParamsTest")
public class BindMetadataToQueryParamsTest {

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void testMustBeMap() {
      BindMetadataToQueryParams binder = new BindMetadataToQueryParams(null);
      HttpRequest request = new HttpRequest(HttpMethod.POST, URI.create("http://localhost"), new char[] { '/', ':' });
      binder.bindToRequest(request, new File("foo"));
   }

   @Test
   public void testCorrect() throws SecurityException, NoSuchMethodException {

      HttpRequest request = new HttpRequest("GET", URI.create("http://momma/"), new char[] { '/', ':' });

      BindMetadataToQueryParams binder = new BindMetadataToQueryParams(new Provider<UriBuilder>() {

         @Override
         public UriBuilder get() {
            return new UriBuilderImpl();
         }

      });

      request = binder.bindToRequest(request, ImmutableMap.of("imageName", "foo", "serverId", "2"));

      assertEquals(request.getRequestLine(), "GET http://momma/?metadata=imagename:foo&metadata=serverid:2 HTTP/1.1");

   }

   @Test(expectedExceptions = { NullPointerException.class, IllegalStateException.class })
   public void testNullIsBad() {
      BindMetadataToQueryParams binder = new BindMetadataToQueryParams(null);
      GeneratedHttpRequest<?> request = createMock(GeneratedHttpRequest.class);
      binder.bindToRequest(request, null);
   }
}
