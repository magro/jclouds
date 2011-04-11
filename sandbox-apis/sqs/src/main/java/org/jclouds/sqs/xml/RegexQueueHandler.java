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
package org.jclouds.sqs.xml;

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.sqs.domain.Queue;
import org.jclouds.sqs.xml.internal.BaseRegexQueueHandler;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.functions.ReturnStringIf2xx;
import org.jclouds.location.Region;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

/**
 * 
 * @see <a href="http://docs.amazonwebservices.com/AWSSimpleQueueService/latest/APIReference/Query_QueryListQueues.html"
 *      />
 * @author Adrian Cole
 */
@Singleton
public class RegexQueueHandler extends BaseRegexQueueHandler implements
         Function<HttpResponse, Queue> {
   private final ReturnStringIf2xx returnStringIf200;

   @Inject
   RegexQueueHandler(@Region Map<String, URI> regionMap, ReturnStringIf2xx returnStringIf200) {
      super(regionMap);
      this.returnStringIf200 = returnStringIf200;
   }

   @Override
   public Queue apply(HttpResponse response) {
      return Iterables.getOnlyElement(parse(returnStringIf200.apply(response)));
   }
}
