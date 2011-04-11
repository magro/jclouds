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
package org.jclouds.rimuhosting.miro.domain;

import com.google.gson.annotations.SerializedName;

/**
 * TODO: test
 *
 * @author Ivan Meredith
 */
public class DataCenter implements Comparable<DataCenter> {
   @SerializedName("data_center_location_code")
   private String id;
   @SerializedName("data_center_location_name")
   private String name;
   @SerializedName("data_center_location_country_2ltr")
   private String code;
   
   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   @Override
   public int compareTo(DataCenter dataCenter) {
      return id.compareTo(dataCenter.getId());
   }

   public void setCode(String code) {
   	this.code = code;
   }
   
   public String getCode() {
      return code;
   }
}
