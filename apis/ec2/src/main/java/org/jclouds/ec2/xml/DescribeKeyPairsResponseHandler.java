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

import java.util.Set;

import javax.inject.Inject;

import org.jclouds.aws.util.AWSUtils;
import org.jclouds.ec2.domain.KeyPair;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.location.Region;

import com.google.common.collect.Sets;

/**
 * Parses: DescribeKeyPairsResponse xmlns="http://ec2.amazonaws.com/doc/2010-06-15/"
 * 
 * @see <a href="http://docs.amazonwebservices.com/AWSEC2/latest/APIReference/ApiReference-query-DescribeKeyPairs.html"
 *      />
 * @author Adrian Cole
 */
public class DescribeKeyPairsResponseHandler extends
         ParseSax.HandlerForGeneratedRequestWithResult<Set<KeyPair>> {
   @Inject
   @Region
   String defaultRegion;

   private StringBuilder currentText = new StringBuilder();
   private Set<KeyPair> keyPairs = Sets.newLinkedHashSet();
   private String keyFingerprint;
   private String keyName;

   public Set<KeyPair> getResult() {
      return keyPairs;
   }

   public void endElement(String uri, String name, String qName) {

      if (qName.equals("keyFingerprint")) {
         this.keyFingerprint = currentText.toString().trim();
      } else if (qName.equals("item")) {
         String region = AWSUtils.findRegionInArgsOrNull(getRequest());
         if (region == null)
            region = defaultRegion;
         keyPairs.add(new KeyPair(region, keyName, keyFingerprint, null));
      } else if (qName.equals("keyName")) {
         this.keyName = currentText.toString().trim();
      }

      currentText = new StringBuilder();
   }

   public void characters(char ch[], int start, int length) {
      currentText.append(ch, start, length);
   }
}
