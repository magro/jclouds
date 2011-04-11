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
package org.jclouds.sqs.xml;

import org.jclouds.crypto.CryptoStreams;
import org.jclouds.http.functions.ParseSax;

/**
 * 
 * @see <a href="http://docs.amazonwebservices.com/AWSSimpleQueueService/latest/APIReference/Query_QuerySendMessage.html"
 *      />
 * @author Adrian Cole
 */
public class MD5Handler extends ParseSax.HandlerWithResult<byte[]> {

   private StringBuilder currentText = new StringBuilder();
   byte[] md5;

   public byte[] getResult() {
      return md5;
   }

   public void endElement(String uri, String name, String qName) {
      if (qName.equals("MD5OfMessageBody")) {
         String md5Hex = currentText.toString().trim();
         this.md5 = CryptoStreams.hex(md5Hex);
      }
      currentText = new StringBuilder();
   }

   public void characters(char ch[], int start, int length) {
      currentText.append(ch, start, length);
   }
}
