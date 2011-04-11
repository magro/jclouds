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
package org.jclouds.gogrid.functions;

import java.util.SortedSet;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.gogrid.domain.Server;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.functions.ParseJson;

import com.google.common.base.Function;

/**
 * Parses {@link Server servers} from a json string.
 * 
 * @author Adrian Cole
 */
@Singleton
public class ParseServerListFromJsonResponse implements
      Function<HttpResponse, SortedSet<Server>> {

   private final ParseJson<GenericResponseContainer<Server>> json;

   @Inject
   ParseServerListFromJsonResponse(ParseJson<GenericResponseContainer<Server>> json) {
      this.json = json;
   }

   @Override
   public SortedSet<Server> apply(HttpResponse arg0) {
      return json.apply(arg0).getList();
   }

}
