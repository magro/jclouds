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
package org.jclouds.nirvanix.sdn;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.integration.internal.BaseBlobStoreIntegrationTest;
import org.jclouds.encryption.internal.Base64;
import org.jclouds.io.Payloads;
import org.jclouds.logging.log4j.config.Log4JLoggingModule;
import org.jclouds.nirvanix.sdn.domain.UploadInfo;
import org.jclouds.rest.RestContext;
import org.jclouds.rest.RestContextFactory;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

/**
 * Tests behavior of {@code SDNClient}
 * 
 * @author Adrian Cole
 */
@Test(groups = "live", sequential = true, testName = "sdn.SDNClientLiveTest")
public class SDNClientLiveTest {

   protected SDNClient connection;
   private String containerPrefix = BaseBlobStoreIntegrationTest.CONTAINER_PREFIX;

   URI container1;
   URI container2;
   private RestContext<SDNClient, SDNAsyncClient> context;

   @BeforeGroups(groups = { "live" })
   public void setupClient() {

      String identity = checkNotNull(System.getProperty("jclouds.test.identity"), "jclouds.test.identity");
      String credential = checkNotNull(System.getProperty("jclouds.test.credential"), "jclouds.test.credential");

      this.context = new RestContextFactory().createContext("sdn", identity, credential, ImmutableSet
               .<Module> of(new Log4JLoggingModule()));
      this.connection = context.getApi();
   }

   public void testUploadToken() throws InterruptedException, ExecutionException, TimeoutException, IOException {
      String containerName = containerPrefix + ".testObjectOperations";
      long size = 1024;

      UploadInfo uploadInfo = connection.getStorageNode(containerName, size);
      assertNotNull(uploadInfo.getHost());
      assertNotNull(uploadInfo.getToken());

      Blob blob = connection.newBlob();
      blob.getMetadata().setName("test.txt");
      blob.setPayload("value");
      Payloads.calculateMD5(blob);

      byte[] md5 = blob.getMetadata().getContentMetadata().getContentMD5();
      connection.upload(uploadInfo.getHost(), uploadInfo.getToken(), containerName, blob);

      Map<String, String> metadata = connection.getMetadata(containerName + "/test.txt");
      assertEquals(metadata.get("MD5"), Base64.encodeBytes(md5));

      String content = connection.getFile(containerName + "/test.txt");
      assertEquals(content, "value");

      metadata = ImmutableMap.of("chef", "sushi", "foo", "bar");
      connection.setMetadata(containerName + "/test.txt", metadata);

      metadata = connection.getMetadata(containerName + "/test.txt");
      assertEquals(metadata.get("MD5"), Base64.encodeBytes(md5));

   }
}
