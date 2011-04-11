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
import org.jclouds.http.functions.ParseJson;
import org.jclouds.ibmdev.config.IBMDeveloperCloudParserModule;
import org.jclouds.ibmdev.domain.Key;
import org.jclouds.io.Payloads;
import org.jclouds.json.config.GsonModule;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;

/**
 * Tests behavior of {@code ParseKeyFromJson}
 * 
 * @author Adrian Cole
 */
@Test(groups = "unit", sequential = true, testName = "ibmdev.ParseKeyFromJsonTest")
public class ParseKeyFromJsonTest {

   private ParseJson<Key> handler;

   @BeforeTest
   protected void setUpInjector() throws IOException {
      Injector injector = Guice.createInjector(new IBMDeveloperCloudParserModule(), new GsonModule());
      handler = injector.getInstance(com.google.inject.Key.get(new TypeLiteral<ParseJson<Key>>() {
      }));
   }

   public void test() {
      Key key = new Key(true, ImmutableSet.<String> of("1"), "AAAB3NzaC1yc2EAAAADAQABAAABAQCqBw7a+...", "DEFAULT",
            new Date(1260428507510l));

      Key compare = handler.apply(new HttpResponse(200, "ok", Payloads.newInputStreamPayload(ParseKeyFromJsonTest.class
            .getResourceAsStream("/key.json"))));
      assertEquals(compare, key);
   }
}
