/**
 *
 * Copyright (C) 2009 Global Cloud Specialists, Inc. <info@globalcloudspecialists.com>
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 */
package org.jclouds.aws.s3.functions;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.io.IOUtils;
import org.jclouds.aws.s3.reference.S3Headers;
import org.jclouds.http.HttpException;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.HttpUtils;

import com.google.common.base.Function;

/**
 * Parses an MD5 checksum from the header {@link S3Headers#ETAG}.
 * 
 * @author Adrian Cole
 */
public class ParseETagHeader implements Function<HttpResponse, byte[]> {

   public byte[] apply(HttpResponse from) {
      IOUtils.closeQuietly(from.getContent());

      String eTag = from.getFirstHeaderOrNull(HttpHeaders.ETAG);
      if (eTag != null) {
         return HttpUtils.fromHexString(eTag.replaceAll("\"", ""));
      }
      throw new HttpException("did not receive ETag");
   }

}