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
package org.jclouds.ec2.compute.domain;

/**
 * 
 * @author Adrian Cole
 */
public class RegionAndName {
   protected final String region;
   protected final String name;

   public RegionAndName(String region, String name) {
      this.region = region;
      this.name = name;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((region == null) ? 0 : region.hashCode());
      result = prime * result + ((name == null) ? 0 : name.hashCode());
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
      RegionAndName other = (RegionAndName) obj;
      if (region == null) {
         if (other.region != null)
            return false;
      } else if (!region.equals(other.region))
         return false;
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      return true;
   }

   public String getRegion() {
      return region;
   }

   public String getName() {
      return name;
   }

   @Override
   public String toString() {
      return "[region=" + region + ", name=" + name + "]";
   }

}
