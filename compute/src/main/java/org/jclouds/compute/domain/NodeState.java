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
package org.jclouds.compute.domain;

/**
 * Indicates the status of a node
 * 
 * @author Adrian Cole
 */
public enum NodeState {
   /**
    * The node is in transition
    */
   PENDING,
   /**
    * The node is visible, and in the process of being deleted.
    */
   TERMINATED,
   /**
    * The node is deployed, but suspended or stopped.
    */
   SUSPENDED,
   /**
    * The node is available for requests
    */
   RUNNING,
   /**
    * There is an error on the node
    */
   ERROR,
   /**
    * The state of the node is unrecognized.
    */
   UNRECOGNIZED;

}