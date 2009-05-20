/**
 *
 * Copyright (C) 2009 Adrian Cole <adrian@jclouds.org>
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 */
package org.jclouds.aws.s3.filters;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.name.Names;
import org.jclouds.aws.s3.reference.S3Constants;
import org.jclouds.aws.s3.util.DateService;
import org.testng.annotations.Test;


@Test(groups = "unit", testName = "s3.RequestAuthorizeSignatureTest")
public class RequestAuthorizeSignatureTest {

    RequestAuthorizeSignature filter = null;

    @Test
    void testUpdatesOnlyOncePerSecond() throws NoSuchMethodException, InterruptedException {
        filter = Guice.createInjector(new AbstractModule() {

            protected void configure() {
                bindConstant().annotatedWith(Names.named(S3Constants.PROPERTY_AWS_ACCESSKEYID)).to("foo");
                bindConstant().annotatedWith(Names.named(S3Constants.PROPERTY_AWS_SECRETACCESSKEY)).to("bar");
                bind(DateService.class);

            }
        }).getInstance(RequestAuthorizeSignature.class);
//        filter.createNewStamp();
        String timeStamp = filter.timestampAsHeaderString();
//        replay(filter);
        for (int i = 0; i < 10; i++)
            filter.updateIfTimeOut();
        assert timeStamp.equals(filter.timestampAsHeaderString());
        Thread.sleep(1000);
        assert !timeStamp.equals(filter.timestampAsHeaderString());
//        verify(filter);
    }


}