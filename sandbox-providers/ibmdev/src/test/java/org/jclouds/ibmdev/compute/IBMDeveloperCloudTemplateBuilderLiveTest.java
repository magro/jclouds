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
package org.jclouds.ibmdev.compute;

import java.util.Set;

import org.jclouds.compute.BaseTemplateBuilderLiveTest;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.OsFamilyVersion64Bit;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;

/**
 * 
 * @author Adrian Cole
 */
@Test(groups = "live", testName = "ibmdev.IBMDeveloperCloudTemplateBuilderLiveTest")
public class IBMDeveloperCloudTemplateBuilderLiveTest extends BaseTemplateBuilderLiveTest {

   public IBMDeveloperCloudTemplateBuilderLiveTest() {
      provider = "ibmdev";
   }

   @Override
   protected Predicate<OsFamilyVersion64Bit> defineUnsupportedOperatingSystems() {
      return new Predicate<OsFamilyVersion64Bit>() {

         @Override
         public boolean apply(OsFamilyVersion64Bit input) {
            return input.family != OsFamily.RHEL && //
                     input.family != OsFamily.SUSE && //
                     input.family != OsFamily.WINDOWS;
         }

      };
   }

   @Override
   protected Set<String> getIso3166Codes() {
      return ImmutableSet.of("US-NC", "DE-BW", "US-CO", "CA-ON");
   }
}