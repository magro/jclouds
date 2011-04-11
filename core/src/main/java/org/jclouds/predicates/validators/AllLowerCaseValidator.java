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
package org.jclouds.predicates.validators;

import org.jclouds.predicates.Validator;

import javax.annotation.Nullable;

/**
 * Validates that the string paremeter doesn't have any uppercase letters.
 * 
 * @see org.jclouds.rest.InputParamValidator
 * @see org.jclouds.predicates.Validator
 */
public class AllLowerCaseValidator extends Validator<String> {

   public void validate(@Nullable String s) {
      if (!(s == null || s.toLowerCase().equals(s))) {
         throw new IllegalArgumentException(String.format(
                  "Object '%s' doesn't match the lower case", s));
      }
   }

}
