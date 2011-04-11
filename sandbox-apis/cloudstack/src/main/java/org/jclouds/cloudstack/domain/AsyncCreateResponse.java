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
package org.jclouds.cloudstack.domain;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author Adrian Cole
 */
public class AsyncCreateResponse {
   private long id;
   @SerializedName("jobid")
   private long jobId;

   /**
    * present only for serializer
    * 
    */
   AsyncCreateResponse() {

   }

   public AsyncCreateResponse(long id, long jobId) {
      this.id = id;
      this.jobId = jobId;
   }

   /**
    * @return id of the resource being created
    */
   public long getId() {
      return id;
   }

   /**
    * @return id of the job in progress
    */
   public long getJobId() {
      return jobId;
   }

}
