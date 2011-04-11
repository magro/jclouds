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
import java.util.Set;

import org.jclouds.http.HttpResponse;
import org.jclouds.ibmdev.config.IBMDeveloperCloudParserModule;
import org.jclouds.ibmdev.domain.Instance;
import org.jclouds.ibmdev.domain.Instance.Software;
import org.jclouds.io.Payloads;
import org.jclouds.json.config.GsonModule;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Tests behavior of {@code ParseInstancesFromJson}
 * 
 * @author Adrian Cole
 */
@Test(groups = "unit", sequential = true, testName = "ibmdev.ParseInstancesFromJsonTest")
public class ParseInstancesFromJsonTest {

   private ParseInstancesFromJson handler;

   @BeforeTest
   protected void setUpInjector() throws IOException {
      Injector injector = Guice.createInjector(new IBMDeveloperCloudParserModule(), new GsonModule());
      handler = injector.getInstance(ParseInstancesFromJson.class);
   }

   public void test() {
      Instance instance1 = new Instance(new Date(1260472231726l), ImmutableSet.of(new Software("SUSE Linux Enterprise",
            "OS", "10 SP2")), "129.33.197.78", "7430", "DEFAULT", "ABC", "MEDIUM", 5, "aadelucc@us.ibm.com",
            "vm723.developer.ihost.com", "1", "3", ImmutableSet.<String> of(), "ABC", "7430", new Date(1263064240837l));

      Instance instance2 = new Instance(new Date(1260472231727l), ImmutableSet.of(new Software("SUSE Linux Enterprise",
            "OS", "10 SP3")), null, "7431", "DEFAULT", "ABC", "MEDIUM", 1, "aadelucc@us.ibm.com", null, "2", "4",
            ImmutableSet.<String> of(), "ABC", "7431", new Date(1263064240838l));

      Set<? extends Instance> compare = handler.apply(new HttpResponse(200, "ok", Payloads
            .newInputStreamPayload(ParseInstancesFromJsonTest.class.getResourceAsStream("/instances.json"))));
      assertEquals(Iterables.get(compare, 1), instance1);
      assertEquals(Iterables.get(compare, 0), instance2);
   }
}
