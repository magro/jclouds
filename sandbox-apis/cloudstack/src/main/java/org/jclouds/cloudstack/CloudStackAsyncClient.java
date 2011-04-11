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
package org.jclouds.cloudstack;

import org.jclouds.cloudstack.features.AddressAsyncClient;
import org.jclouds.cloudstack.features.AsyncJobAsyncClient;
import org.jclouds.cloudstack.features.ConfigurationAsyncClient;
import org.jclouds.cloudstack.features.FirewallAsyncClient;
import org.jclouds.cloudstack.features.GuestOSAsyncClient;
import org.jclouds.cloudstack.features.HypervisorAsyncClient;
import org.jclouds.cloudstack.features.LoadBalancerAsyncClient;
import org.jclouds.cloudstack.features.NATAsyncClient;
import org.jclouds.cloudstack.features.NetworkAsyncClient;
import org.jclouds.cloudstack.features.OfferingAsyncClient;
import org.jclouds.cloudstack.features.SecurityGroupAsyncClient;
import org.jclouds.cloudstack.features.TemplateAsyncClient;
import org.jclouds.cloudstack.features.VirtualMachineAsyncClient;
import org.jclouds.cloudstack.features.ZoneAsyncClient;
import org.jclouds.rest.annotations.Delegate;

/**
 * Provides asynchronous access to CloudStack via their REST API.
 * <p/>
 * 
 * @see CloudStackClient
 * @see <a href="http://download.cloud.com/releases/2.2.0/api/TOC_User.html" />
 * @author Adrian Cole
 */
public interface CloudStackAsyncClient {

   /**
    * Provides asynchronous access to Zone features.
    */
   @Delegate
   ZoneAsyncClient getZoneClient();

   /**
    * Provides asynchronous access to Template features.
    */
   @Delegate
   TemplateAsyncClient getTemplateClient();

   /**
    * Provides asynchronous access to Service, Disk, and Network Offering features.
    */
   @Delegate
   OfferingAsyncClient getOfferingClient();

   /**
    * Provides asynchronous access to Network features.
    */
   @Delegate
   NetworkAsyncClient getNetworkClient();

   /**
    * Provides asynchronous access to VirtualMachine features.
    */
   @Delegate
   VirtualMachineAsyncClient getVirtualMachineClient();

   /**
    * Provides asynchronous access to SecurityGroup features.
    */
   @Delegate
   SecurityGroupAsyncClient getSecurityGroupClient();

   /**
    * Provides asynchronous access to AsyncJob features.
    */
   @Delegate
   AsyncJobAsyncClient getAsyncJobClient();

   /**
    * Provides asynchronous access to Address features.
    */
   @Delegate
   AddressAsyncClient getAddressClient();

   /**
    * Provides asynchronous access to NAT features.
    */
   @Delegate
   NATAsyncClient getNATClient();

   /**
    * Provides asynchronous access to Firewall features.
    */
   @Delegate
   FirewallAsyncClient getFirewallClient();

   /**
    * Provides asynchronous access to LoadBalancer features.
    */
   @Delegate
   LoadBalancerAsyncClient getLoadBalancerClient();

   /**
    * Provides asynchronous access to GuestOS features.
    */
   @Delegate
   GuestOSAsyncClient getGuestOSClient();

   /**
    * Provides asynchronous access to Hypervisor features.
    */
   @Delegate
   HypervisorAsyncClient getHypervisorClient();

   /**
    * Provides asynchronous access to Configuration features.
    */
   @Delegate
   ConfigurationAsyncClient getConfigurationClient();
}
