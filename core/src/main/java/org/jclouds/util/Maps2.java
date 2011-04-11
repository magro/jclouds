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
package org.jclouds.util;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Maps.filterKeys;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;

/**
 * General utilities used in jclouds code for {@link Map}s.
 * 
 * @author Adrian Cole
 */
public class Maps2 {

   /**
    * If the supplied map contains the key {@code k1}, its value will be assigned to the key
    * {@code k2}. Note that this doesn't modify the input map.
    * 
    * @param <V>
    *           type of value the map holds
    * @param in
    *           the map you wish to make a copy of
    * @param k1
    *           old key
    * @param k2
    *           new key
    * @return copy of the map with the value of the key re-routed, or the original, if it {@code k1}
    *         wasn't present.
    */
   public static <V> Map<String, V> renameKey(Map<String, V> in, String k1, String k2) {
      if (checkNotNull(in, "input map").containsKey(checkNotNull(k1, "old key"))) {
         Builder<String, V> builder = ImmutableMap.builder();
         builder.putAll(filterKeys(in, not(equalTo(k1))));
         V tags = in.get(k1);
         builder.put(checkNotNull(k2, "new key"), tags);
         in = builder.build();
      }
      return in;
   }

   /**
    * change the keys but keep the values in-tact.
    * 
    * @param <K1>
    *           input key type
    * @param <K2>
    *           output key type
    * @param <V>
    *           value type
    * @param in
    *           input map to transform
    * @param fn
    *           how to transform the values
    * @return immutableMap with the new keys.
    */
   public static <K1, K2, V> Map<K2, V> transformKeys(Map<K1, V> in, Function<K1, K2> fn) {
      checkNotNull(in, "input map");
      checkNotNull(fn, "function");
      Builder<K2, V> returnVal = ImmutableMap.builder();
      for (Entry<K1, V> entry : in.entrySet())
         returnVal.put(fn.apply(entry.getKey()), entry.getValue());
      return returnVal.build();
   }

   public static <K, V> Supplier<Map<K, V>> composeMapSupplier(Iterable<Supplier<Map<K, V>>> suppliers) {
      return new ListMapSupplier<K, V>(suppliers);
   }

   static class ListMapSupplier<K, V> implements Supplier<Map<K, V>> {

      private final Iterable<Supplier<Map<K, V>>> suppliers;

      ListMapSupplier(Iterable<Supplier<Map<K, V>>> suppliers) {
         this.suppliers = checkNotNull(suppliers, "suppliers");
      }

      @Override
      public Map<K, V> get() {
         Map<K, V> toReturn = Maps.newLinkedHashMap();
         for (Supplier<Map<K, V>> supplier : suppliers) {
            toReturn.putAll(supplier.get());
         }
         return toReturn;
      }
   }

}
