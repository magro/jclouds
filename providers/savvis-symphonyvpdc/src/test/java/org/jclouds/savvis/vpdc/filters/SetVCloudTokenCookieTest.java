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

import static org.testng.Assert.assertEquals;

import java.net.URI;

import javax.inject.Provider;
import javax.ws.rs.core.HttpHeaders;

import org.jclouds.http.HttpRequest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author Adrian Cole
 */
@Test
public class SetVCloudTokenCookieTest {

   private SetVCloudTokenCookie filter;

   @BeforeTest
   void setUp() {
      filter = new SetVCloudTokenCookie(new Provider<String>() {
         public String get() {
            return "token";
         }
      });
   }

   @Test
   public void testApply() {
      HttpRequest request = new HttpRequest("GET", URI.create("http://localhost"));
      request = filter.filter(request);
      assertEquals(request.getHeaders().size(), 1);
      assertEquals(request.getFirstHeaderOrNull(HttpHeaders.COOKIE), "vcloud-token=token");

   }

}
