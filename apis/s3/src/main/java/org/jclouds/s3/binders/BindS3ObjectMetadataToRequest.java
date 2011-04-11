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
package org.jclouds.s3.binders;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.HttpHeaders;

import org.jclouds.blobstore.binders.BindMapToHeadersWithPrefix;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.utils.ModifyRequest;
import org.jclouds.rest.Binder;
import org.jclouds.s3.domain.S3Object;

/**
 * 
 * @author Adrian Cole
 */
@Singleton
public class BindS3ObjectMetadataToRequest implements Binder {
   protected final BindMapToHeadersWithPrefix metadataPrefixer;

   @Inject
   public BindS3ObjectMetadataToRequest(BindMapToHeadersWithPrefix metadataPrefixer) {
      this.metadataPrefixer = checkNotNull(metadataPrefixer, "metadataPrefixer");
   }

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      checkArgument(checkNotNull(input, "input") instanceof S3Object, "this binder is only valid for S3Object!");
      checkNotNull(request, "request");

      S3Object s3Object = S3Object.class.cast(input);
      checkArgument(s3Object.getMetadata().getKey() != null, "s3Object.getMetadata().getKey() must be set!");
      checkArgument(s3Object.getPayload().getContentMetadata().getContentLength() != null,
            "contentLength must be set, streaming not supported");
      checkArgument(s3Object.getPayload().getContentMetadata().getContentLength() <= 5l * 1024 * 1024 * 1024,
            "maximum size for put object is 5GB");
      
      request = metadataPrefixer.bindToRequest(request, s3Object.getMetadata().getUserMetadata());

      if (s3Object.getMetadata().getCacheControl() != null) {
         request = ModifyRequest.replaceHeader(request, HttpHeaders.CACHE_CONTROL, s3Object.getMetadata()
               .getCacheControl());
      }
      return request;
   }
}
