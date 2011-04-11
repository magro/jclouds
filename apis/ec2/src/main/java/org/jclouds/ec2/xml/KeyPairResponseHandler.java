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
package org.jclouds.ec2.xml;

import javax.inject.Inject;

import org.jclouds.aws.util.AWSUtils;
import org.jclouds.ec2.domain.KeyPair;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.location.Region;

/**
 * 
 * @see <a href=
 *      "http://docs.amazonwebservices.com/AWSEC2/latest/APIReference/ApiReference-query-CreateKeyPair.html"
 *      />
 * @author Adrian Cole
 */
public class KeyPairResponseHandler extends ParseSax.HandlerForGeneratedRequestWithResult<KeyPair> {
   @Inject
   @Region
   String defaultRegion;
   private StringBuilder currentText = new StringBuilder();
   private String keyFingerprint;
   private String keyMaterial;
   private String keyName;

   public KeyPair getResult() {
      String region = AWSUtils.findRegionInArgsOrNull(getRequest());
      if (region == null)
         region = defaultRegion;
      return new KeyPair(region, keyName, keyFingerprint, keyMaterial);
   }

   public void endElement(String uri, String name, String qName) {

      if (qName.equals("keyFingerprint")) {
         this.keyFingerprint = currentText.toString().trim();
      } else if (qName.equals("keyMaterial")) {
         this.keyMaterial = currentText.toString().trim();
      } else if (qName.equals("keyName")) {
         this.keyName = currentText.toString().trim();
      }

      currentText = new StringBuilder();
   }

   public void characters(char ch[], int start, int length) {
      currentText.append(ch, start, length);
   }
}
