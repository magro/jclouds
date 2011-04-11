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
package org.jclouds.savvis.vpdc.features;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;

import org.jclouds.http.HttpRequest;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;
import org.jclouds.rest.internal.RestAnnotationProcessor;
import org.jclouds.savvis.vpdc.options.GetVMOptions;
import org.jclouds.savvis.vpdc.xml.FirewallServiceHandler;
import org.jclouds.savvis.vpdc.xml.NetworkHandler;
import org.jclouds.savvis.vpdc.xml.OrgHandler;
import org.jclouds.savvis.vpdc.xml.TaskHandler;
import org.jclouds.savvis.vpdc.xml.VDCHandler;
import org.jclouds.savvis.vpdc.xml.VMHandler;
import org.testng.annotations.Test;

import com.google.inject.TypeLiteral;

/**
 * Tests annotation parsing of {@code BrowsingAsyncClient}
 * 
 * @author Adrian Cole
 */
@Test(groups = "unit")
public class BrowsingAsyncClientTest extends BaseVPDCAsyncClientTest<BrowsingAsyncClient> {

   public void testOrg() throws SecurityException, NoSuchMethodException, IOException {
      Method method = BrowsingAsyncClient.class.getMethod("getOrg", String.class);
      HttpRequest request = processor.createRequest(method, "11");

      assertRequestLineEquals(request, "GET https://api.symphonyvpdc.savvis.net/rest/api/v0.8/org/11 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseSax.class);
      assertSaxResponseParserClassEquals(method, OrgHandler.class);
      assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

      checkFilters(request);
   }

   public void testOrgNull() throws SecurityException, NoSuchMethodException, IOException {
      Method method = BrowsingAsyncClient.class.getMethod("getOrg", String.class);
      HttpRequest request = processor.createRequest(method, (String) null);

      assertRequestLineEquals(request, "GET https://api.symphonyvpdc.savvis.net/rest/api/v0.8/org/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseSax.class);
      assertSaxResponseParserClassEquals(method, OrgHandler.class);
      assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

      checkFilters(request);
   }

   public void testVDC() throws SecurityException, NoSuchMethodException, IOException {
      Method method = BrowsingAsyncClient.class.getMethod("getVDCInOrg", String.class, String.class);
      HttpRequest request = processor.createRequest(method, "11", "22");

      assertRequestLineEquals(request, "GET https://api.symphonyvpdc.savvis.net/rest/api/v0.8/org/11/vdc/22 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseSax.class);
      assertSaxResponseParserClassEquals(method, VDCHandler.class);
      assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

      checkFilters(request);
   }

   public void testVDCWhenOrgNull() throws SecurityException, NoSuchMethodException, IOException {
      Method method = BrowsingAsyncClient.class.getMethod("getVDCInOrg", String.class, String.class);
      HttpRequest request = processor.createRequest(method, (String) null, "22");

      assertRequestLineEquals(request, "GET https://api.symphonyvpdc.savvis.net/rest/api/v0.8/org/1/vdc/22 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseSax.class);
      assertSaxResponseParserClassEquals(method, VDCHandler.class);
      assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

      checkFilters(request);
   }

   public void testNetwork() throws SecurityException, NoSuchMethodException, IOException {
      Method method = BrowsingAsyncClient.class.getMethod("getNetworkInVDC", String.class, String.class, String.class);
      HttpRequest request = processor.createRequest(method, "11", "22", "VM-Tier01");

      assertRequestLineEquals(request,
               "GET https://api.symphonyvpdc.savvis.net/rest/api/v0.8/org/11/vdc/22/network/VM-Tier01 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseSax.class);
      assertSaxResponseParserClassEquals(method, NetworkHandler.class);
      assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

      checkFilters(request);
   }

   public void testNetworkWhenOrgNull() throws SecurityException, NoSuchMethodException, IOException {
      Method method = BrowsingAsyncClient.class.getMethod("getNetworkInVDC", String.class, String.class, String.class);
      HttpRequest request = processor.createRequest(method, (String) null, "22", "VM-Tier01");

      assertRequestLineEquals(request,
               "GET https://api.symphonyvpdc.savvis.net/rest/api/v0.8/org/1/vdc/22/network/VM-Tier01 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseSax.class);
      assertSaxResponseParserClassEquals(method, NetworkHandler.class);
      assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

      checkFilters(request);
   }

   public void testVMVDC() throws SecurityException, NoSuchMethodException, IOException {
      Method method = BrowsingAsyncClient.class.getMethod("getVMInVDC", String.class, String.class, String.class,
               GetVMOptions[].class);
      HttpRequest request = processor.createRequest(method, "11", "22", "33");

      assertRequestLineEquals(request,
               "GET https://api.symphonyvpdc.savvis.net/rest/api/v0.8/org/11/vdc/22/vApp/33 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseSax.class);
      assertSaxResponseParserClassEquals(method, VMHandler.class);
      assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

      checkFilters(request);
   }

   public void testVM() throws SecurityException, NoSuchMethodException, IOException {
      Method method = BrowsingAsyncClient.class.getMethod("getVM", URI.class, GetVMOptions[].class);
      HttpRequest request = processor.createRequest(method, URI
               .create("https://api.symphonyvpdc.savvis.net/rest/api/v0.8/org/11/vdc/22/vApp/33"));

      assertRequestLineEquals(request,
               "GET https://api.symphonyvpdc.savvis.net/rest/api/v0.8/org/11/vdc/22/vApp/33 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseSax.class);
      assertSaxResponseParserClassEquals(method, VMHandler.class);
      assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

      checkFilters(request);
   }

   public void testVMWithPowerState() throws SecurityException, NoSuchMethodException, IOException {
      Method method = BrowsingAsyncClient.class.getMethod("getVMInVDC", String.class, String.class, String.class,
               GetVMOptions[].class);
      HttpRequest request = processor.createRequest(method, "11", "22", "VM-Tier01", GetVMOptions.Builder
               .withPowerState());

      assertRequestLineEquals(request,
               "GET https://api.symphonyvpdc.savvis.net/rest/api/v0.8/org/11/vdc/22/vApp/VM-Tier01/withpowerstate HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseSax.class);
      assertSaxResponseParserClassEquals(method, VMHandler.class);
      assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

      checkFilters(request);
   }

   public void testVMWhenOrgNull() throws SecurityException, NoSuchMethodException, IOException {
      Method method = BrowsingAsyncClient.class.getMethod("getVMInVDC", String.class, String.class, String.class,
               GetVMOptions[].class);
      HttpRequest request = processor.createRequest(method, (String) null, "22", "VM-Tier01");

      assertRequestLineEquals(request,
               "GET https://api.symphonyvpdc.savvis.net/rest/api/v0.8/org/1/vdc/22/vApp/VM-Tier01 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseSax.class);
      assertSaxResponseParserClassEquals(method, VMHandler.class);
      assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

      checkFilters(request);
   }

   public void testGetTask() throws SecurityException, NoSuchMethodException, IOException {
      Method method = BrowsingAsyncClient.class.getMethod("getTask", String.class);
      HttpRequest request = processor.createRequest(method, "1");

      assertRequestLineEquals(request, "GET https://api.symphonyvpdc.savvis.net/rest/api/v0.8/task/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseSax.class);
      assertSaxResponseParserClassEquals(method, TaskHandler.class);
      assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

      checkFilters(request);
   }

   public void testListFirewallRules() throws SecurityException, NoSuchMethodException, IOException {
      Method method = BrowsingAsyncClient.class.getMethod("listFirewallRules", String.class, String.class);
      HttpRequest request = processor.createRequest(method, "11", "22");

      assertRequestLineEquals(request,
               "GET https://api.symphonyvpdc.savvis.net/rest/api/v0.8/org/11/vdc/22/FirewallService HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseSax.class);
      assertSaxResponseParserClassEquals(method, FirewallServiceHandler.class);
      assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

      checkFilters(request);
   }

   @Override
   protected TypeLiteral<RestAnnotationProcessor<BrowsingAsyncClient>> createTypeLiteral() {
      return new TypeLiteral<RestAnnotationProcessor<BrowsingAsyncClient>>() {
      };
   }

}
