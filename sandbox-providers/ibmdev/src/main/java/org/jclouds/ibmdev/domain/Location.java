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
package org.jclouds.ibmdev.domain;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 
 * The current state of a location (datacenter)
 * 
 * @author Adrian Cole
 */
public class Location {

   private final String id;
   private final String name;
   private final String description;
   private final String location;
   private final Map<String, Map<String, String>> capabilities = Maps.newLinkedHashMap();

   public Location(String id, String name, String description, String location,
            Map<String, Map<String, String>> capabilities) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.location = location;
      this.capabilities.putAll(capabilities);
   }

   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public String getDescription() {
      return description;
   }

   public String getLocation() {
      return location;
   }

   public Map<String, Map<String, String>> getCapabilities() {
      return capabilities;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((capabilities == null) ? 0 : capabilities.hashCode());
      result = prime * result + ((description == null) ? 0 : description.hashCode());
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      result = prime * result + ((location == null) ? 0 : location.hashCode());
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
      Location other = (Location) obj;
      if (capabilities == null) {
         if (other.capabilities != null)
            return false;
      } else if (!capabilities.equals(other.capabilities))
         return false;
      if (description == null) {
         if (other.description != null)
            return false;
      } else if (!description.equals(other.description))
         return false;
      if (id == null) {
         if (other.id != null)
            return false;
      } else if (!id.equals(other.id))
         return false;
      if (location == null) {
         if (other.location != null)
            return false;
      } else if (!location.equals(other.location))
         return false;
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      return true;
   }

   @Override
   public String toString() {
      return "Location [id=" + id + ", name=" + name + ", description=" + description
               + ", location=" + location + ", capabilities=" + capabilities + "]";
   }

}