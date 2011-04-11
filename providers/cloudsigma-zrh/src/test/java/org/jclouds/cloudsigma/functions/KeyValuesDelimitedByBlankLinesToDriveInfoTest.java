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
package org.jclouds.cloudsigma.functions;

import static org.testng.Assert.assertEquals;

import org.jclouds.http.HttpResponse;
import org.jclouds.io.Payloads;
import org.testng.annotations.Test;

import com.google.inject.Guice;

/**
 * 
 * @author Adrian Cole
 */
@Test(groups = { "unit" })
public class KeyValuesDelimitedByBlankLinesToDriveInfoTest {

   private static final KeyValuesDelimitedByBlankLinesToDriveInfo FN = Guice.createInjector().getInstance(
         KeyValuesDelimitedByBlankLinesToDriveInfo.class);

   public void testNone() {
      assertEquals(FN.apply(new HttpResponse(200, "", Payloads.newStringPayload(""))), null);
      assertEquals(FN.apply(new HttpResponse(200, "", Payloads.newStringPayload("\n\n"))), null);
      assertEquals(FN.apply(new HttpResponse(200, "", null)), null);
   }

   public void testOne() {
      assertEquals(FN.apply(new HttpResponse(200, "", Payloads.newInputStreamPayload(MapToDriveInfoTest.class
            .getResourceAsStream("/drive.txt")))), MapToDriveInfoTest.ONE);
   }
}
