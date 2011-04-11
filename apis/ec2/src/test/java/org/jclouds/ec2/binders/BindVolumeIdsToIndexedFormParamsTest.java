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
package org.jclouds.ec2.binders;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.net.URI;

import javax.ws.rs.HttpMethod;

import org.jclouds.http.HttpRequest;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Tests behavior of {@code BindVolumeIdsToIndexedFormParams}
 * 
 * @author Adrian Cole
 */
@Test(groups = "unit")
public class BindVolumeIdsToIndexedFormParamsTest {
   Injector injector = Guice.createInjector();
   BindVolumeIdsToIndexedFormParams binder = injector.getInstance(BindVolumeIdsToIndexedFormParams.class);

   public void test() {

      HttpRequest request = HttpRequest.builder().method("POST").endpoint(URI.create("http://localhost")).build();
      request = binder.bindToRequest(request, new String[] { "alpha", "omega" });
      assertEquals(request.getPayload().getRawContent(), "VolumeId.1=alpha&VolumeId.2=omega");
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void testMustBeArray() {
      HttpRequest request = new HttpRequest(HttpMethod.POST, URI.create("http://localhost"));
      binder.bindToRequest(request, new File("foo"));
   }

   @Test(expectedExceptions = NullPointerException.class)
   public void testNullIsBad() {
      HttpRequest request = HttpRequest.builder().method("GET").endpoint(URI.create("http://momma")).build();
      binder.bindToRequest(request, null);
   }
}
