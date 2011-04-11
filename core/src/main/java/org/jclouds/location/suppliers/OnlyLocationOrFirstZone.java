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
package org.jclouds.location.suppliers;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Iterables.getOnlyElement;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.collect.Memoized;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationScope;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;

/**
 * 
 * @author Adrian Cole
 * 
 */
@Singleton
public class OnlyLocationOrFirstZone implements Supplier<Location> {
   @Singleton
   public static final class IsZone implements Predicate<Location> {
      @Override
      public boolean apply(Location input) {
         return input.getScope() == LocationScope.ZONE;
      }

      @Override
      public String toString() {
         return "isZone()";
      }
   }

   private final Supplier<Set<? extends Location>> locationsSupplier;
   private final IsZone isZone;

   @Inject
   OnlyLocationOrFirstZone(@Memoized Supplier<Set<? extends Location>> locationsSupplier, IsZone isZone) {
      this.locationsSupplier = checkNotNull(locationsSupplier, "locationsSupplierSupplier");
      this.isZone = checkNotNull(isZone, "isZone");
   }

   @Override
   public Location get() {
      if (locationsSupplier.get().size() == 1)
         return getOnlyElement(locationsSupplier.get());
      return find(locationsSupplier.get(), isZone);
   }

}