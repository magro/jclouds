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
package org.jclouds.ibmdev.functions;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;

import org.jclouds.http.HttpResponse;
import org.jclouds.ibmdev.config.IBMDeveloperCloudParserModule;
import org.jclouds.ibmdev.domain.Volume;
import org.jclouds.io.Payloads;
import org.jclouds.json.config.GsonModule;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Tests behavior of {@code ParseVolumeFromJson}
 * 
 * @author Adrian Cole
 */
@Test(groups = "unit", sequential = true, testName = "ibmdev.ParseVolumeFromJsonTest")
public class ParseVolumeFromJsonTest {

   private ParseVolumeFromJson handler;

   @BeforeTest
   protected void setUpInjector() throws IOException {
      Injector injector = Guice.createInjector(new IBMDeveloperCloudParserModule(), new GsonModule());
      handler = injector.getInstance(ParseVolumeFromJson.class);
   }

   public void test() {

      Volume volume = new Volume("2", 5, 50, "aadelucc@us.ibm.com", new Date(1260469075119l), "1", ImmutableSet
            .<String> of(), "ext3", "New Storage", "67");

      Volume compare = handler.apply(new HttpResponse(200, "ok", Payloads
            .newInputStreamPayload(ParseVolumeFromJsonTest.class.getResourceAsStream("/volume.json"))));
      assertEquals(compare, volume);
   }
}
