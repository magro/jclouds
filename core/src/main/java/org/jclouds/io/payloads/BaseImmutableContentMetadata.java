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
package org.jclouds.io.payloads;

import java.io.Serializable;
import java.util.Arrays;

import org.jclouds.io.ContentMetadata;
import org.jclouds.io.ContentMetadataBuilder;

/**
 * @author Adrian Cole
 */
public class BaseImmutableContentMetadata implements ContentMetadata, Serializable {

   /** The serialVersionUID */
   private static final long serialVersionUID = -1445533440795766130L;
   protected String contentType;
   protected Long contentLength;
   protected byte[] contentMD5;
   protected String contentDisposition;
   protected String contentLanguage;
   protected String contentEncoding;

   public BaseImmutableContentMetadata(String contentType, Long contentLength, byte[] contentMD5,
            String contentDisposition, String contentLanguage, String contentEncoding) {
      this.contentType = contentType;
      this.contentLength = contentLength;
      this.contentMD5 = contentMD5;
      this.contentDisposition = contentDisposition;
      this.contentLanguage = contentLanguage;
      this.contentEncoding = contentEncoding;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Long getContentLength() {
      return contentLength;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public byte[] getContentMD5() {
      if (contentMD5 != null) {
         byte[] retval = new byte[contentMD5.length];
         System.arraycopy(this.contentMD5, 0, retval, 0, contentMD5.length);
         return retval;
      } else {
         return null;
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getContentType() {
      return contentType;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getContentDisposition() {
      return this.contentDisposition;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getContentLanguage() {
      return this.contentLanguage;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getContentEncoding() {
      return this.contentEncoding;
   }

   @Override
   public String toString() {
      return "[contentType=" + contentType + ", contentLength=" + contentLength + ", contentDisposition="
               + contentDisposition + ", contentEncoding=" + contentEncoding + ", contentLanguage=" + contentLanguage
               + ", contentMD5=" + Arrays.toString(contentMD5) + "]";
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((contentDisposition == null) ? 0 : contentDisposition.hashCode());
      result = prime * result + ((contentEncoding == null) ? 0 : contentEncoding.hashCode());
      result = prime * result + ((contentLanguage == null) ? 0 : contentLanguage.hashCode());
      result = prime * result + ((contentLength == null) ? 0 : contentLength.hashCode());
      result = prime * result + Arrays.hashCode(contentMD5);
      result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      BaseImmutableContentMetadata other = (BaseImmutableContentMetadata) obj;
      if (contentDisposition == null) {
         if (other.contentDisposition != null)
            return false;
      } else if (!contentDisposition.equals(other.contentDisposition))
         return false;
      if (contentEncoding == null) {
         if (other.contentEncoding != null)
            return false;
      } else if (!contentEncoding.equals(other.contentEncoding))
         return false;
      if (contentLanguage == null) {
         if (other.contentLanguage != null)
            return false;
      } else if (!contentLanguage.equals(other.contentLanguage))
         return false;
      if (contentLength == null) {
         if (other.contentLength != null)
            return false;
      } else if (!contentLength.equals(other.contentLength))
         return false;
      if (!Arrays.equals(contentMD5, other.contentMD5))
         return false;
      if (contentType == null) {
         if (other.contentType != null)
            return false;
      } else if (!contentType.equals(other.contentType))
         return false;
      return true;
   }

   @Override
   public ContentMetadataBuilder toBuilder() {
      return ContentMetadataBuilder.fromContentMetadata(this);
   }

}