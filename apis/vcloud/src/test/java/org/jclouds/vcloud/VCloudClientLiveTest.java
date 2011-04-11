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
package org.jclouds.vcloud;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.jclouds.rest.AuthorizationException;
import org.jclouds.vcloud.domain.Catalog;
import org.jclouds.vcloud.domain.CatalogItem;
import org.jclouds.vcloud.domain.Org;
import org.jclouds.vcloud.domain.ReferenceType;
import org.jclouds.vcloud.domain.VApp;
import org.jclouds.vcloud.domain.VDC;
import org.jclouds.vcloud.domain.Vm;
import org.testng.annotations.Test;

/**
 * Tests behavior of {@code VCloudClient}
 * 
 * @author Adrian Cole
 */
@Test(groups = "live", sequential = true)
public class VCloudClientLiveTest extends CommonVCloudClientLiveTest<VCloudClient, VCloudAsyncClient> {

   @Test
   public void testListOrgs() throws Exception {
      for (ReferenceType response : connection.listOrgs().values()) {
         assertNotNull(response);
         assertNotNull(response.getName());
         assertNotNull(response.getHref());
         assertEquals(connection.getOrg(response.getHref()).getName(), response.getName());
         assertEquals(connection.findOrgNamed(response.getName()).getName(), response.getName());
      }
   }

   @Test
   public void testGetVAppTemplate() throws Exception {
      Org org = connection.findOrgNamed(null);
      for (ReferenceType cat : org.getCatalogs().values()) {
         Catalog response = connection.getCatalog(cat.getHref());
         for (ReferenceType resource : response.values()) {
            if (resource.getType().equals(VCloudMediaType.CATALOGITEM_XML)) {
               CatalogItem item = connection.getCatalogItem(resource.getHref());
               if (item.getEntity().getType().equals(VCloudMediaType.VAPPTEMPLATE_XML)) {
                  try {
                     assertNotNull(connection.getVAppTemplate(item.getEntity().getHref()));
                  } catch (AuthorizationException e) {

                  }
               }
            }
         }
      }
   }

   @Test
   public void testGetOvfEnvelopeForVAppTemplate() throws Exception {
      Org org = connection.findOrgNamed(null);
      for (ReferenceType cat : org.getCatalogs().values()) {
         Catalog response = connection.getCatalog(cat.getHref());
         for (ReferenceType resource : response.values()) {
            if (resource.getType().equals(VCloudMediaType.CATALOGITEM_XML)) {
               try {
                  CatalogItem item = connection.getCatalogItem(resource.getHref());
                  if (item.getEntity().getType().equals(VCloudMediaType.VAPPTEMPLATE_XML)) {
                     assertNotNull(connection.getOvfEnvelopeForVAppTemplate(item.getEntity().getHref()));
                  }
               } catch (AuthorizationException e) {

               }
            }
         }
      }
   }

   @Test
   public void testGetVApp() throws Exception {
      Org org = connection.findOrgNamed(null);
      for (ReferenceType vdc : org.getVDCs().values()) {
         VDC response = connection.getVDC(vdc.getHref());
         for (ReferenceType item : response.getResourceEntities().values()) {
            if (item.getType().equals(VCloudMediaType.VAPP_XML)) {
               try {
                  VApp app = connection.getVApp(item.getHref());
                  assertNotNull(app);
               } catch (RuntimeException e) {

               }
            }
         }
      }
   }

   @Test
   public void testGetThumbnailOfVm() throws Exception {
      Org org = connection.findOrgNamed(null);
      for (ReferenceType vdc : org.getVDCs().values()) {
         VDC response = connection.getVDC(vdc.getHref());
         for (ReferenceType item : response.getResourceEntities().values()) {
            if (item.getType().equals(VCloudMediaType.VAPP_XML)) {
               try {
                  VApp app = connection.getVApp(item.getHref());
                  assertNotNull(app);
                  for (Vm vm : app.getChildren()) {
                     assert connection.getThumbnailOfVm(vm.getHref()) != null;
                  }
               } catch (RuntimeException e) {

               }
            }
         }
      }
   }

   @Test
   public void testGetVm() throws Exception {
      Org org = connection.findOrgNamed(null);
      for (ReferenceType vdc : org.getVDCs().values()) {
         VDC response = connection.getVDC(vdc.getHref());
         for (ReferenceType item : response.getResourceEntities().values()) {
            if (item.getType().equals(VCloudMediaType.VAPP_XML)) {
               try {
                  VApp app = connection.getVApp(item.getHref());
                  assertNotNull(app);
                  for (Vm vm : app.getChildren()) {
//                     assertEquals(connection.getVm(vm.getHref()), vm);
                  }
               } catch (RuntimeException e) {

               }
            }
         }
      }
   }

   @Test
   public void testFindVAppTemplate() throws Exception {
      Org org = connection.findOrgNamed(null);
      for (ReferenceType cat : org.getCatalogs().values()) {
         Catalog response = connection.getCatalog(cat.getHref());
         for (ReferenceType resource : response.values()) {
            if (resource.getType().equals(VCloudMediaType.CATALOGITEM_XML)) {
               CatalogItem item = connection.getCatalogItem(resource.getHref());
               if (item.getEntity().getType().equals(VCloudMediaType.VAPPTEMPLATE_XML)) {
                  try {
                     assertNotNull(connection.findVAppTemplateInOrgCatalogNamed(org.getName(), response.getName(), item
                           .getEntity().getName()));
                  } catch (AuthorizationException e) {

                  }
               }
            }
         }
      }
   }
}
