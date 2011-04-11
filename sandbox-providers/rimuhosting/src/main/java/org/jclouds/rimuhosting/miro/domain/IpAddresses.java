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

import java.util.SortedSet;

/**
 * IpAddresses assigned to an Interface. Not rimuhosting doesnt have private IPs.
 *
 * @author Ivan Meredith
 */
public class IpAddresses {
   @SerializedName("primary_ip")
   private String primaryIp;
   @SerializedName("secondary_ips")
   private SortedSet<String> secondaryIps;

   public String getPrimaryIp() {
      return primaryIp;
   }

   public void setPrimaryIp(String primaryIp) {
      this.primaryIp = primaryIp;
   }

   public SortedSet<String> getSecondaryIps() {
      return secondaryIps;
   }

   public void setSecondaryIps(SortedSet<String> secondaryIps) {
      this.secondaryIps = secondaryIps;
   }
}
