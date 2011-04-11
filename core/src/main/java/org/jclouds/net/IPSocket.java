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
package org.jclouds.net;

import java.io.Serializable;

/**
 * As google appengine prohibits use of java.net classes, this will serve as a replacement.
 * 
 * @author Adrian Cole
 */
public class IPSocket implements Serializable {

   /** The serialVersionUID */
   private static final long serialVersionUID = 2978329372952402188L;

   private final String address;
   private final int port;

   public IPSocket(String address, int port) {
      this.address = address;
      this.port = port;
   }

   public String getAddress() {
      return address;
   }

   public int getPort() {
      return port;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((address == null) ? 0 : address.hashCode());
      result = prime * result + port;
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
      IPSocket other = (IPSocket) obj;
      if (address == null) {
         if (other.address != null)
            return false;
      } else if (!address.equals(other.address))
         return false;
      if (port != other.port)
         return false;
      return true;
   }

   @Override
   public String toString() {
      return "[address=" + address + ", port=" + port + "]";
   }

}
