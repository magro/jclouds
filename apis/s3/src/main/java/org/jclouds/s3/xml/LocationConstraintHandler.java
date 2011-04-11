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
package org.jclouds.s3.xml;

import static org.jclouds.util.SaxUtils.currentOrNull;

import org.jclouds.aws.domain.Region;
import org.jclouds.http.functions.ParseSax;

/**
 * Parses the response from Amazon S3 GET Bucket Location
 * <p/>
 * Region is the document we expect to parse.
 * 
 * @see <a href= "http://docs.amazonwebservices.com/AmazonS3/latest/RESTBucketLocationGET.html" />
 * @author Adrian Cole
 */
public class LocationConstraintHandler extends ParseSax.HandlerWithResult<String> {
   private StringBuilder currentText = new StringBuilder();
   private String region;

   public String getResult() {
      return region;
   }

   public void endElement(String uri, String name, String qName) {
      region = fromValue(currentOrNull(currentText));
   }

   /**
    * parses the value expected in xml documents from the S3 service.=
    * <p/>
    * {@code US_STANDARD} is returned as "" xml documents.
    */
   public static String fromValue(String v) {
      if (v == null || "".equals(v))
         return Region.US_STANDARD;
      return v;
   }

   public void characters(char ch[], int start, int length) {
      currentText.append(ch, start, length);
   }
}
