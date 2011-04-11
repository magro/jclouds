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
package org.jclouds.azureblob;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.jclouds.azureblob.options.CreateContainerOptions.Builder.withMetadata;
import static org.jclouds.azureblob.options.CreateContainerOptions.Builder.withPublicAccess;
import static org.jclouds.azure.storage.options.ListOptions.Builder.includeMetadata;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URI;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.Set;

import org.jclouds.Constants;
import org.jclouds.azure.storage.AzureStorageResponseException;
import org.jclouds.azureblob.domain.AzureBlob;
import org.jclouds.azureblob.domain.BlobProperties;
import org.jclouds.azureblob.domain.ContainerProperties;
import org.jclouds.azureblob.domain.ListBlobsResponse;
import org.jclouds.azureblob.domain.PublicAccess;
import org.jclouds.azureblob.options.ListBlobsOptions;
import org.jclouds.azure.storage.domain.BoundedSet;
import org.jclouds.azure.storage.options.ListOptions;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.BlobStoreContextFactory;
import org.jclouds.blobstore.ContainerNotFoundException;
import org.jclouds.crypto.CryptoStreams;
import org.jclouds.http.HttpResponseException;
import org.jclouds.http.options.GetOptions;
import org.jclouds.io.Payloads;
import org.jclouds.logging.log4j.config.Log4JLoggingModule;
import org.jclouds.util.Strings2;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.inject.Module;

/**
 * Tests behavior of {@code AzureBlobClient}
 * 
 * @author Adrian Cole
 */
@Test(groups = "live", sequential = true)
public class AzureBlobClientLiveTest {

   protected AzureBlobClient client;

   private String containerPrefix = System.getProperty("user.name") + "-azureblob";

   private BlobStoreContext context;
   protected String provider = "azureblob";
   protected String identity;
   protected String credential;
   protected String endpoint;
   protected String apiversion;

   protected void setupCredentials() {
      identity = checkNotNull(System.getProperty("test." + provider + ".identity"), "test." + provider + ".identity");
      credential = System.getProperty("test." + provider + ".credential");
      endpoint = System.getProperty("test." + provider + ".endpoint");
      apiversion = System.getProperty("test." + provider + ".apiversion");
   }

   protected Properties setupProperties() {
      Properties overrides = new Properties();
      overrides.setProperty(Constants.PROPERTY_TRUST_ALL_CERTS, "true");
      overrides.setProperty(Constants.PROPERTY_RELAX_HOSTNAME, "true");
      overrides.setProperty(provider + ".identity", identity);
      if (credential != null)
         overrides.setProperty(provider + ".credential", credential);
      if (endpoint != null)
         overrides.setProperty(provider + ".endpoint", endpoint);
      if (apiversion != null)
         overrides.setProperty(provider + ".apiversion", apiversion);
      return overrides;
   }

   @BeforeGroups(groups = { "live" })
   public void setupClient() {
      setupCredentials();
      Properties overrides = setupProperties();
      context = new BlobStoreContextFactory().createContext(provider, ImmutableSet
               .<Module> of(new Log4JLoggingModule()), overrides);
      client = (AzureBlobClient) context.getProviderSpecificContext().getApi();
   }

   @Test
   public void testListContainers() throws Exception {
      Set<ContainerProperties> response = client.listContainers();
      assert null != response;
      long initialContainerCount = response.size();
      assertTrue(initialContainerCount >= 0);

   }

   String privateContainer;
   String publicContainer;

   @Test(timeOut = 5 * 60 * 1000)
   public void testCreateContainer() throws Exception {
      boolean created = false;
      while (!created) {
         privateContainer = containerPrefix + new SecureRandom().nextInt();
         try {
            created = client.createContainer(privateContainer, withMetadata(ImmutableMultimap.of("foo", "bar")));
         } catch (UndeclaredThrowableException e) {
            HttpResponseException htpe = (HttpResponseException) e.getCause().getCause();
            if (htpe.getResponse().getStatusCode() == 409)
               continue;
            throw e;
         }
      }
      Set<ContainerProperties> response = client.listContainers(includeMetadata());
      assert null != response;
      long containerCount = response.size();
      assertTrue(containerCount >= 1);
      ListBlobsResponse list = client.listBlobs(privateContainer);
      assertEquals(list.getUrl(), URI.create(String.format("https://%s.blob.core.windows.net/%s", identity,
               privateContainer)));
      // TODO .. check to see the container actually exists
   }

   @Test(timeOut = 5 * 60 * 1000)
   public void testCreatePublicContainer() throws Exception {
      boolean created = false;
      while (!created) {
         publicContainer = containerPrefix + new SecureRandom().nextInt();
         try {
            created = client.createContainer(publicContainer, withPublicAccess(PublicAccess.BLOB));
         } catch (UndeclaredThrowableException e) {
            HttpResponseException htpe = (HttpResponseException) e.getCause().getCause();
            if (htpe.getResponse().getStatusCode() == 409)
               continue;
            throw e;
         }
      }
      // TODO
      // URL url = new URL(String.format("http://%s.blob.core.windows.net/%s", identity,
      // publicContainer));
      // Utils.toStringAndClose(url.openStream());
   }

   @Test(timeOut = 5 * 60 * 1000)
   public void testCreatePublicRootContainer() throws Exception {
      try {
         client.deleteRootContainer();
      } catch (ContainerNotFoundException e) {
         Thread.sleep(5000);
      } catch (AzureStorageResponseException htpe) {
         if (htpe.getResponse().getStatusCode() == 409) {// TODO look for specific message
            Thread.sleep(5000);
         } else {
            throw htpe;
         }
      }

      boolean created = false;
      while (!created) {
         try {
            created = client.createRootContainer();
         } catch (AzureStorageResponseException htpe) {
            if (htpe.getResponse().getStatusCode() == 409) {// TODO look for specific message
               Thread.sleep(5000);
               continue;
            } else {
               throw htpe;
            }
         }
      }
      ListBlobsResponse list = client.listBlobs();
      assertEquals(list.getUrl(), URI.create(String.format("https://%s.blob.core.windows.net/$root", identity)));
   }

   @Test
   public void testListContainersWithOptions() throws Exception {

      BoundedSet<ContainerProperties> response = client.listContainers(ListOptions.Builder.prefix(privateContainer)
               .maxResults(1).includeMetadata());
      assert null != response;
      long initialContainerCount = response.size();
      assertTrue(initialContainerCount >= 0);
      assertEquals(privateContainer, response.getPrefix());
      assertEquals(1, response.getMaxResults());
   }

   @Test(timeOut = 5 * 60 * 1000, dependsOnMethods = { "testCreatePublicRootContainer" })
   public void testDeleteRootContainer() throws Exception {
      client.deleteRootContainer();
      // TODO loop for up to 30 seconds checking if they are really gone
   }

   @Test(timeOut = 5 * 60 * 1000, dependsOnMethods = { "testCreateContainer", "testCreatePublicContainer" })
   public void testListOwnedContainers() throws Exception {

      // Test default listing
      Set<ContainerProperties> response = client.listContainers();
      // assertEquals(response.size(), initialContainerCount + 2);// if the containers already
      // exist, this will fail

      // Test listing with options
      response = client.listContainers(ListOptions.Builder.prefix(
               privateContainer.substring(0, privateContainer.length() - 1)).maxResults(1).includeMetadata());
      assertEquals(response.size(), 1);
      assertEquals(Iterables.getOnlyElement(response).getName(), privateContainer);
      assertEquals(Iterables.getOnlyElement(response).getMetadata(), ImmutableMap.of("foo", "bar"));

      response = client.listContainers(ListOptions.Builder.prefix(publicContainer).maxResults(1));
      assertEquals(response.size(), 1);
      assertEquals(Iterables.getOnlyElement(response).getName(), publicContainer);

   }

   @Test
   public void testDeleteOneContainer() throws Exception {
      client.deleteContainer("does-not-exist");
   }

   @Test(timeOut = 5 * 60 * 1000, dependsOnMethods = { "testListOwnedContainers", "testObjectOperations" })
   public void testDeleteContainer() throws Exception {
      client.deleteContainer(privateContainer);
      client.deleteContainer(publicContainer);
      // TODO loop for up to 30 seconds checking if they are really gone
   }

   @Test(timeOut = 5 * 60 * 1000, dependsOnMethods = { "testCreateContainer", "testCreatePublicContainer" })
   public void testObjectOperations() throws Exception {
      String data = "Here is my data";

      // Test PUT with string data, ETag hash, and a piece of metadata
      AzureBlob object = client.newBlob();
      object.getProperties().setName("object");
      object.setPayload(data);
      Payloads.calculateMD5(object);
      object.getProperties().getContentMetadata().setContentType("text/plain");
      object.getProperties().getMetadata().put("mykey", "metadata-value");
      byte[] md5 = object.getProperties().getContentMetadata().getContentMD5();
      String newEtag = client.putBlob(privateContainer, object);
      assertEquals(CryptoStreams.hex(md5), CryptoStreams.hex(object.getProperties().getContentMetadata()
               .getContentMD5()));

      // Test HEAD of missing object
      assert client.getBlobProperties(privateContainer, "non-existent-object") == null;

      // Test HEAD of object
      BlobProperties metadata = client.getBlobProperties(privateContainer, object.getProperties().getName());
      // TODO assertEquals(metadata.getName(), object.getProperties().getName());
      // we can't check this while hacking around lack of content-md5, as GET of the first byte will
      // show incorrect length 1, the returned size, as opposed to the real length. This is an ok
      // tradeoff, as a container list will contain the correct size of the objects in an
      // inexpensive fashion
      // http://code.google.com/p/jclouds/issues/detail?id=92
      // assertEquals(metadata.getSize(), data.length());
      assertEquals(metadata.getContentMetadata().getContentType(), "text/plain");
      // Azure doesn't return the Content-MD5 on head request..
      assertEquals(CryptoStreams.hex(md5), CryptoStreams.hex(object.getProperties().getContentMetadata()
               .getContentMD5()));
      assertEquals(metadata.getETag(), newEtag);
      assertEquals(metadata.getMetadata().entrySet().size(), 1);
      assertEquals(metadata.getMetadata().get("mykey"), "metadata-value");

      // // Test POST to update object's metadata
      // Multimap<String, String> userMetadata = LinkedHashMultimap.create();
      // userMetadata.put("New-Metadata-1", "value-1");
      // userMetadata.put("New-Metadata-2", "value-2");
      // assertTrue(client.setBlobProperties(privateContainer, object.getProperties().getName(),
      // userMetadata));

      // Test GET of missing object
      assert client.getBlob(privateContainer, "non-existent-object") == null;

      // Test GET of object (including updated metadata)
      AzureBlob getBlob = client.getBlob(privateContainer, object.getProperties().getName());
      assertEquals(Strings2.toStringAndClose(getBlob.getPayload().getInput()), data);
      // TODO assertEquals(getBlob.getName(), object.getProperties().getName());
      assertEquals(getBlob.getPayload().getContentMetadata().getContentLength(), new Long(data.length()));
      assertEquals(getBlob.getProperties().getContentMetadata().getContentType(), "text/plain");
      assertEquals(CryptoStreams.hex(md5), CryptoStreams.hex(getBlob.getProperties().getContentMetadata()
               .getContentMD5()));
      assertEquals(newEtag, getBlob.getProperties().getETag());
      // wait until we can update metadata
      // assertEquals(getBlob.getProperties().getMetadata().entries().size(), 2);
      // assertEquals(
      // Iterables.getLast(getBlob.getProperties().getMetadata().get("New-Metadata-1")),
      // "value-1");
      // assertEquals(
      // Iterables.getLast(getBlob.getProperties().getMetadata().get("New-Metadata-2")),
      // "value-2");
      assertEquals(metadata.getMetadata().entrySet().size(), 1);
      assertEquals(metadata.getMetadata().get("mykey"), "metadata-value");

      // test listing
      ListBlobsResponse response = client.listBlobs(privateContainer, ListBlobsOptions.Builder.prefix(
               object.getProperties().getName().substring(0, object.getProperties().getName().length() - 1))
               .maxResults(1).includeMetadata());
      assertEquals(response.size(), 1);
      assertEquals(Iterables.getOnlyElement(response).getName(), object.getProperties().getName());
      assertEquals(Iterables.getOnlyElement(response).getMetadata(), ImmutableMap.of("mykey", "metadata-value"));

      // Test PUT with invalid ETag (as if object's data was corrupted in transit)
      String correctEtag = newEtag;
      String incorrectEtag = "0" + correctEtag.substring(1);
      object.getProperties().setETag(incorrectEtag);
      try {
         client.putBlob(privateContainer, object);
      } catch (Throwable e) {
         assertEquals(e.getCause().getClass(), HttpResponseException.class);
         assertEquals(((HttpResponseException) e.getCause()).getResponse().getStatusCode(), 422);
      }

      ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes("UTF-8"));
      object = client.newBlob();
      object.getProperties().setName("chunked-object");
      object.setPayload(bais);
      object.getPayload().getContentMetadata().setContentLength(new Long(data.getBytes().length));
      newEtag = client.putBlob(privateContainer, object);
      assertEquals(CryptoStreams.hex(md5), CryptoStreams.hex(getBlob.getProperties().getContentMetadata()
               .getContentMD5()));

      // Test GET with options
      // Non-matching ETag
      try {
         client.getBlob(privateContainer, object.getProperties().getName(), GetOptions.Builder
                  .ifETagDoesntMatch(newEtag));
      } catch (Exception e) {
         assertEquals(e.getCause().getClass(), HttpResponseException.class);
         assertEquals(((HttpResponseException) e.getCause()).getResponse().getStatusCode(), 304);
      }

      // Matching ETag TODO this shouldn't fail!!!
      try {
         getBlob = client.getBlob(privateContainer, object.getProperties().getName(), GetOptions.Builder
                  .ifETagMatches(newEtag));
         assertEquals(getBlob.getProperties().getETag(), newEtag);
      } catch (HttpResponseException e) {
         assertEquals(e.getResponse().getStatusCode(), 412);
      }

      // Range
      // doesn't work per
      // http://social.msdn.microsoft.com/Forums/en-US/windowsazure/thread/479fa63f-51df-4b66-96b5-33ae362747b6
      // getBlob = client
      // .getBlob(privateContainer, object.getProperties().getName(),
      // GetOptions.Builder.startAt(8)).get(120,
      // TimeUnit.SECONDS);
      // assertEquals(Utils.toStringAndClose((InputStream) getBlob.getData()), data.substring(8));

      client.deleteBlob(privateContainer, "object");
      client.deleteBlob(privateContainer, "chunked-object");
   }
}
