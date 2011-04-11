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
package org.jclouds.aws.config;

import static org.testng.Assert.assertEquals;

import java.net.URI;
import java.util.Map;

import org.jclouds.logging.jdk.JDKLogger;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

/**
 * 
 * @author Adrian Cole
 */
@Test(sequential = true, groups = { "unit" })
public class AWSWithZonesFormSigningRestClientModuleTest {

   @Test
   public void testDefaultRegionWhenThereIsAMatch() {
      AWSRestClientModule<String,String> module = new WithZonesFormSigningRestClientModule<String,String>(
            String.class, String.class);

      URI currentEndpoint = URI.create("http://region1");
      Map<String, URI> map = ImmutableMap.of("region1", currentEndpoint, "region2", URI.create("http://region2"));

      assertEquals("region1", module.getDefaultRegion(currentEndpoint, map, new JDKLogger.JDKLoggerFactory()));
   }

   @Test
   public void testDefaultRegionWhenThereIsNoMatch() {
      AWSRestClientModule<String,String> module = new WithZonesFormSigningRestClientModule<String,String>(
            String.class, String.class);

      URI currentEndpoint = URI.create("http://region3");
      Map<String, URI> map = ImmutableMap.of("region1", currentEndpoint, "region2", URI.create("http://region2"));

      assertEquals("region1", module.getDefaultRegion(currentEndpoint, map, new JDKLogger.JDKLoggerFactory()));
   }

}
