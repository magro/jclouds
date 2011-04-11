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
package org.jclouds.vcloud.terremark.compute.suppliers;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Sets.newLinkedHashSet;

import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.collect.Memoized;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.ImageBuilder;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.domain.Location;
import org.jclouds.logging.Logger;
import org.jclouds.vcloud.compute.functions.ImagesInVCloudExpressOrg;
import org.jclouds.vcloud.domain.Org;

import com.google.common.base.Function;
import com.google.common.base.Supplier;

/**
 * @author Adrian Cole
 */
@Singleton
public class VAppTemplatesInOrgs implements Supplier<Set<? extends Image>> {

   @Resource
   @Named(ComputeServiceConstants.COMPUTE_LOGGER)
   public Logger logger = Logger.NULL;

   private final Supplier<Set<? extends Location>> locations;
   private final Function<Iterable<? extends Location>, Iterable<? extends Org>> organizatonsForLocations;
   private final ImagesInVCloudExpressOrg imagesInOrg;

   @Inject
   VAppTemplatesInOrgs(@Memoized Supplier<Set<? extends Location>> locations,
            Function<Iterable<? extends Location>, Iterable<? extends Org>> organizatonsForLocations,
            ImagesInVCloudExpressOrg imagesInOrg) {
      this.locations = locations;
      this.organizatonsForLocations = organizatonsForLocations;
      this.imagesInOrg = imagesInOrg;
   }

   /**
    * Terremark does not provide vApp templates in the vDC resourceEntity list. Rather, you must
    * query the catalog.
    */
   @Override
   public Set<? extends Image> get() {
      logger.debug(">> providing vAppTemplates");
      return newLinkedHashSet(transform(
               concat(transform(organizatonsForLocations.apply(locations.get()), imagesInOrg)),
               new Function<Image, Image>() {

                  @Override
                  public Image apply(Image from) {
                     ImageBuilder builder = ImageBuilder.fromImage(from);
                     // the password in the image is the sudo password
                     // TODO refactor authenticate image logic so that it can populate the
                     // adminPassword
                     // value
                     // independently
                     if (from.getDefaultCredentials() != null)
                        builder.adminPassword(from.getDefaultCredentials().credential);
                     return builder.build();
                  }

               }));
   }
}