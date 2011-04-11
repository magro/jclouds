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
package org.jclouds.aws.ec2;

import static org.jclouds.Constants.PROPERTY_ENDPOINT;
import static org.jclouds.aws.ec2.reference.AWSEC2Constants.PROPERTY_EC2_CC_AMIs;
import static org.jclouds.compute.reference.ComputeServiceConstants.PROPERTY_TIMEOUT_NODE_SUSPENDED;
import static org.jclouds.ec2.reference.EC2Constants.PROPERTY_EC2_AMI_OWNERS;

import java.util.Properties;

import org.jclouds.aws.domain.Region;

/**
 * Builds properties used in EC2 Clients
 * 
 * @author Adrian Cole
 */
public class AWSEC2PropertiesBuilder extends org.jclouds.ec2.EC2PropertiesBuilder {
   @Override
   protected Properties defaultProperties() {
      Properties properties = super.defaultProperties();
      // sometimes, like in ec2, stop takes a very long time, perhaps
      // due to volume management. one example spent 2 minutes moving
      // from stopping->stopped state on an ec2 micro
      properties.setProperty(PROPERTY_TIMEOUT_NODE_SUSPENDED, 120 * 1000 + "");
      // auth fail sometimes happens in EC2, as the rc.local script that injects the
      // authorized key executes after ssh has started
      properties.setProperty("jclouds.ssh.max_retries", "7");
      properties.setProperty("jclouds.ssh.retryable_messages",
            "Auth fail,invalid data,End of IO Stream Read,Connection reset,socket is not established,connection is closed by foreign host,socket is not established");
      properties.setProperty(PROPERTY_ENDPOINT, "https://ec2.us-east-1.amazonaws.com");
      properties.putAll(Region.regionProperties());
      // amazon, alestic, canonical, and rightscale
      properties.setProperty(PROPERTY_EC2_AMI_OWNERS, "137112412989,063491364108,099720109477,411009282317");
      // amis that work with the cluster instances
      properties.setProperty(PROPERTY_EC2_CC_AMIs, "us-east-1/ami-321eed5b,us-east-1/ami-7ea24a17");
      return properties;
   }

   public AWSEC2PropertiesBuilder() {
      super();
   }

   public AWSEC2PropertiesBuilder(Properties properties) {
      super(properties);
   }

}
