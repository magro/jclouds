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
package org.jclouds.vcloud.binders;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import org.jclouds.http.HttpRequest;
import org.jclouds.vcloud.VCloudPropertiesBuilder;
import org.jclouds.vcloud.domain.NetworkConnection;
import org.jclouds.vcloud.domain.NetworkConnectionSection;
import org.jclouds.vcloud.domain.network.IpAddressAllocationMode;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

/**
 * Tests behavior of {@code BindNetworkConnectionSectionToXmlPayload}
 * 
 * @author Adrian Cole
 */
@Test(groups = "unit")
public class BindNetworkConnectionSectionToXmlPayloadTest {
   Injector injector = Guice.createInjector(new AbstractModule() {

      @Override
      protected void configure() {
         Properties props = new Properties();
         Names.bindProperties(binder(), checkNotNull(new VCloudPropertiesBuilder(props).build(), "properties"));
      }
   });

   public void testWithIpAllocationModeNONE() throws IOException {

      @SuppressWarnings("rawtypes")
      HttpRequest request = new HttpRequest.Builder().endpoint(URI.create("http://localhost/key")).method("GET")
            .build();

      BindNetworkConnectionSectionToXmlPayload binder = injector
            .getInstance(BindNetworkConnectionSectionToXmlPayload.class);

      binder.bindToRequest(
            request,
            NetworkConnectionSection
                  .builder()
                  .type("application/vnd.vmware.vcloud.networkConnectionSection+xml")
                  .info("Specifies the available VM network connections")
                  .href(URI.create("https://1.1.1.1/api/v1.0/vApp/vm-1/networkConnectionSection/"))
                  .connections(
                        ImmutableSet.<NetworkConnection> of(NetworkConnection.builder().network("none")
                              .ipAddressAllocationMode(IpAddressAllocationMode.NONE).build())).build());
      assertEquals(request.getPayload().getContentMetadata().getContentType(),
            "application/vnd.vmware.vcloud.networkConnectionSection+xml");

      assertEquals(
            request.getPayload().getRawContent(),
            "<NetworkConnectionSection xmlns=\"http://www.vmware.com/vcloud/v1\" xmlns:ovf=\"http://schemas.dmtf.org/ovf/envelope/1\" href=\"https://1.1.1.1/api/v1.0/vApp/vm-1/networkConnectionSection/\" ovf:required=\"false\" type=\"application/vnd.vmware.vcloud.networkConnectionSection+xml\"><ovf:Info>Specifies the available VM network connections</ovf:Info><NetworkConnection network=\"none\"><NetworkConnectionIndex>0</NetworkConnectionIndex><IsConnected>false</IsConnected><IpAddressAllocationMode>NONE</IpAddressAllocationMode></NetworkConnection></NetworkConnectionSection>");

   }

}
