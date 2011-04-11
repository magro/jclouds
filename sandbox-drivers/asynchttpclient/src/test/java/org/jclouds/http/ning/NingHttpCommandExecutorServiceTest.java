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
package org.jclouds.http.ning;

import static org.jclouds.Constants.PROPERTY_CONNECTION_TIMEOUT;
import static org.jclouds.Constants.PROPERTY_IO_WORKER_THREADS;
import static org.jclouds.Constants.PROPERTY_MAX_CONNECTIONS_PER_CONTEXT;
import static org.jclouds.Constants.PROPERTY_MAX_CONNECTIONS_PER_HOST;
import static org.jclouds.Constants.PROPERTY_SO_TIMEOUT;
import static org.jclouds.Constants.PROPERTY_USER_THREADS;

import java.util.Properties;

import org.jclouds.http.BaseHttpCommandExecutorServiceIntegrationTest;
import org.jclouds.http.ning.config.NingHttpCommandExecutorServiceModule;

import com.google.inject.Module;

/**
 * Tests the functionality of the {@link ApacheHCHttpCommandExecutorService}
 * 
 * @author Adrian Cole
 */
public class NingHttpCommandExecutorServiceTest extends BaseHttpCommandExecutorServiceIntegrationTest {
   static {
      System.setProperty("http.conn-manager.timeout", 1000 + "");
   }

   protected Module createConnectionModule() {
      return new NingHttpCommandExecutorServiceModule();
   }

   protected void addConnectionProperties(Properties props) {
      props.setProperty(PROPERTY_MAX_CONNECTIONS_PER_CONTEXT, 20 + "");
      props.setProperty(PROPERTY_MAX_CONNECTIONS_PER_HOST, 0 + "");
      props.setProperty(PROPERTY_CONNECTION_TIMEOUT, 100 + "");
      props.setProperty(PROPERTY_SO_TIMEOUT, 100 + "");
      props.setProperty(PROPERTY_IO_WORKER_THREADS, 3 + "");
      props.setProperty(PROPERTY_USER_THREADS, 0 + "");
   }
}
