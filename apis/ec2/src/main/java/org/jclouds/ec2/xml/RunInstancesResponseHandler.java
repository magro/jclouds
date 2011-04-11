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
package org.jclouds.ec2.xml;

import javax.inject.Inject;

import org.jclouds.date.DateService;
import org.jclouds.ec2.domain.Reservation;
import org.jclouds.ec2.domain.RunningInstance;
import org.jclouds.location.Region;

import com.google.inject.Provider;

/**
 * Parses the following XML document:
 * <p/>
 * RunInstancesResponse xmlns="http:
 * 
 * @author Adrian Cole
 * @see <a href="http: />
 */
public class RunInstancesResponseHandler extends BaseReservationHandler<Reservation<? extends RunningInstance>> {

   @Inject
   RunInstancesResponseHandler(DateService dateService, @Region String defaultRegion,
            Provider<RunningInstance.Builder> builderProvider) {
      super(dateService, defaultRegion, builderProvider);
   }

   @Override
   public Reservation<? extends RunningInstance> getResult() {
      return newReservation();
   }

}
