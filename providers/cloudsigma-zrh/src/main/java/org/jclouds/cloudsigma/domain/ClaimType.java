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
package org.jclouds.cloudsigma.domain;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * either 'exclusive' (the default) or 'shared' to allow multiple servers to access a drive
 * simultaneously
 * 
 * @author Adrian Cole
 */
public enum ClaimType {
   /**
    * 
    */
   EXCLUSIVE,
   /**
    * allow multiple servers to access a drive simultaneously
    */
   SHARED, UNRECOGNIZED;

   public String value() {
      return name().toLowerCase();
   }

   @Override
   public String toString() {
      return value();
   }

   public static ClaimType fromValue(String claim) {
      try {
         return valueOf(checkNotNull(claim, "claim").toUpperCase());
      } catch (IllegalArgumentException e) {
         return UNRECOGNIZED;
      }
   }

}