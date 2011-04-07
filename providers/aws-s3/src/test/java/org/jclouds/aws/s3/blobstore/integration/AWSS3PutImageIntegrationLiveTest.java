/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
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

package org.jclouds.aws.s3.blobstore.integration;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

import org.jclouds.Constants;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.BlobStoreContextFactory;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.io.Payloads;
import org.jclouds.io.payloads.FilePayload;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.Module;

/**
 * A test case that shows the issue with "IOException: Error writing request body to server",
 * as discussed on the mailing list (http://groups.google.com/group/jclouds/browse_thread/thread/4a7c8d58530b287f).
 * <p>
 * To run this test via maven follow the documentation (http://code.google.com/p/jclouds/wiki/DevelopersGuide#Invoking_the_tests)
 * or when running via eclipse add the vm arguments <code>-Dtest.aws-s3.identity=${identity} -Dtest.aws-s3.credential=${credential}</code>
 * to the run configuration.
 * </p> 
 * 
 * @author Martin Grotzke
 */
@Test(groups = "live", testName = "AWSS3PutImageIntegrationLiveTest")
public class AWSS3PutImageIntegrationLiveTest {

    private static final String S3_BUCKET = "jclouds-test";
    private BlobStore s3BlobStore = null;
    private BlobStoreContext s3BlobStoreContext = null;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        Properties overrides = new Properties();
        overrides.setProperty(Constants.PROPERTY_MAX_CONNECTIONS_PER_CONTEXT,
                20 + "");
        overrides.setProperty(Constants.PROPERTY_MAX_CONNECTIONS_PER_HOST,
                0 + "");
        overrides.setProperty(Constants.PROPERTY_CONNECTION_TIMEOUT, 5000 + "");
        overrides.setProperty(Constants.PROPERTY_SO_TIMEOUT, 5000 + "");
        overrides.setProperty(Constants.PROPERTY_IO_WORKER_THREADS, 20 + "");
        // unlimited user threads
        overrides.setProperty(Constants.PROPERTY_USER_THREADS, 0 + "");

        String identity = System.getProperty("test.aws-s3.identity");
        String credential = System.getProperty("test.aws-s3.credential");

        BlobStoreContextFactory bsFactory = new BlobStoreContextFactory();
        s3BlobStoreContext = bsFactory.createContext("aws-s3",
                identity,
                credential, Collections.<Module> emptySet(), overrides);

        s3BlobStore = s3BlobStoreContext.getBlobStore();
        assertNotNull(s3BlobStore);
    }
    
    @AfterMethod
    public void afterMethod() {
        s3BlobStoreContext.close();
    }

    @Test(groups = { "integration", "live" }, invocationCount = 1, threadPoolSize = 1 )
    public void testPutImage() throws InterruptedException, IOException {
        
        File testImg = new File( getClass().getResource( "/testimg.png" ).getFile() );
        assertTrue( testImg.exists() );
        
        // a name per thread to avoid conflicts
        String blobName = Thread.currentThread().getName() + "-testimg.png";
        
        // it's deprecated, but still used in our code
        final Blob blob = s3BlobStore.newBlob( blobName );
        final FilePayload payload = Payloads.newFilePayload( testImg );
        payload.getContentMetadata().setContentType( "image/png" );
        blob.setPayload( payload );
        s3BlobStore.putBlob( S3_BUCKET, blob );
        
        assertTrue( s3BlobStore.blobExists( S3_BUCKET, blobName ) );

        s3BlobStore.removeBlob( S3_BUCKET, blobName );
        
        assertFalse( s3BlobStore.blobExists( S3_BUCKET, blobName ) );
        
    }
    
}
