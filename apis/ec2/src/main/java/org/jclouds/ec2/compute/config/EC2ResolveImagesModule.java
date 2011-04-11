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
package org.jclouds.ec2.compute.config;

import com.google.inject.AbstractModule;

import org.jclouds.ec2.compute.strategy.EC2PopulateDefaultLoginCredentialsForImageStrategy;
import org.jclouds.compute.config.ResolvesImages;
import org.jclouds.compute.strategy.PopulateDefaultLoginCredentialsForImageStrategy;

/**
 * @author Oleksiy Yarmula
 */
@ResolvesImages
public class EC2ResolveImagesModule extends AbstractModule {
    
    @Override
    protected void configure() {
        bind(PopulateDefaultLoginCredentialsForImageStrategy.class).to(EC2PopulateDefaultLoginCredentialsForImageStrategy.class);
    }
    
}
