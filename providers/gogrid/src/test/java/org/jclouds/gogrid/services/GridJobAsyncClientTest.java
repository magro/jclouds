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
package org.jclouds.gogrid.services;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

import org.jclouds.gogrid.domain.JobState;
import org.jclouds.gogrid.domain.ObjectType;
import org.jclouds.gogrid.functions.ParseJobListFromJsonResponse;
import org.jclouds.gogrid.options.GetJobListOptions;
import org.jclouds.http.HttpRequest;
import org.jclouds.rest.internal.RestAnnotationProcessor;
import org.testng.annotations.Test;

import com.google.common.collect.Iterables;
import com.google.inject.TypeLiteral;

/**
 * Tests behavior of {@code GridJobAsyncClient}
 * 
 * @author Oleksiy Yarmula
 */
// NOTE:without testName, this will not call @Before* and fail w/NPE during surefire
@Test(groups = "unit", testName = "GridJobAsyncClientTest")
public class GridJobAsyncClientTest extends BaseGoGridAsyncClientTest<GridJobAsyncClient> {

   @Test
   public void testGetJobListWithOptions() throws NoSuchMethodException, IOException {
      Method method = GridJobAsyncClient.class.getMethod("getJobList", GetJobListOptions[].class);
      HttpRequest httpRequest = processor.createRequest(method,
            new GetJobListOptions.Builder().create().withStartDate(new Date(1267385381770L)).withEndDate(
                  new Date(1267385382770L)).onlyForObjectType(ObjectType.VIRTUAL_SERVER).onlyForState(
                  JobState.PROCESSING));

      assertRequestLineEquals(httpRequest,
            "GET https://api.gogrid.com/api/grid/job/list?v=1.5&startdate=1267385381770&"
                  + "enddate=1267385382770&job.objecttype=VirtualServer&" + "job.state=Processing HTTP/1.1");
      assertNonPayloadHeadersEqual(httpRequest, "");
      assertPayloadEquals(httpRequest, null, null, false);

      assertResponseParserClassEquals(method, httpRequest, ParseJobListFromJsonResponse.class);
      assertSaxResponseParserClassEquals(method, null);
      assertExceptionParserClassEquals(method, null);

      checkFilters(httpRequest);
      httpRequest = Iterables.getOnlyElement(httpRequest.getFilters()).filter(httpRequest);

      assertRequestLineEquals(httpRequest,
            "GET https://api.gogrid.com/api/grid/job/list?v=1.5&startdate=1267385381770&"
                  + "enddate=1267385382770&job.objecttype=VirtualServer&" + "job.state=Processing&"
                  + "sig=3f446f171455fbb5574aecff4997b273&api_key=foo HTTP/1.1");
      assertNonPayloadHeadersEqual(httpRequest, "");
      assertPayloadEquals(httpRequest, null, null, false);
   }

   @Test
   public void testGetJobListNoOptions() throws NoSuchMethodException, IOException {
      Method method = GridJobAsyncClient.class.getMethod("getJobList", GetJobListOptions[].class);
      HttpRequest httpRequest = processor.createRequest(method);

      assertRequestLineEquals(httpRequest, "GET https://api.gogrid.com/api/grid/job/list?v=1.5 HTTP/1.1");
      assertNonPayloadHeadersEqual(httpRequest, "");
      assertPayloadEquals(httpRequest, null, null, false);
   }

   @Test
   public void testGetJobsForServerName() throws NoSuchMethodException, IOException {
      Method method = GridJobAsyncClient.class.getMethod("getJobsForObjectName", String.class);
      HttpRequest httpRequest = processor.createRequest(method, "MyServer");

      assertRequestLineEquals(httpRequest, "GET https://api.gogrid.com/api/grid/job/list?v=1.5&"
            + "object=MyServer HTTP/1.1");
      assertNonPayloadHeadersEqual(httpRequest, "");
      assertPayloadEquals(httpRequest, null, null, false);

      assertResponseParserClassEquals(method, httpRequest, ParseJobListFromJsonResponse.class);
      assertSaxResponseParserClassEquals(method, null);
      assertExceptionParserClassEquals(method, null);

      checkFilters(httpRequest);
      httpRequest = Iterables.getOnlyElement(httpRequest.getFilters()).filter(httpRequest);

      assertRequestLineEquals(httpRequest, "GET https://api.gogrid.com/api/grid/job/list?v=1.5&"
            + "object=MyServer&sig=3f446f171455fbb5574aecff4997b273&api_key=foo " + "HTTP/1.1");
      assertNonPayloadHeadersEqual(httpRequest, "");
      assertPayloadEquals(httpRequest, null, null, false);
   }

   @Test
   public void testGetJobsById() throws NoSuchMethodException, IOException {
      Method method = GridJobAsyncClient.class.getMethod("getJobsById", long[].class);
      HttpRequest httpRequest = processor.createRequest(method, 123L, 456L);

      assertRequestLineEquals(httpRequest, "GET https://api.gogrid.com/api/grid/job/get?v=1.5&"
            + "id=123&id=456 HTTP/1.1");
      assertNonPayloadHeadersEqual(httpRequest, "");
      assertPayloadEquals(httpRequest, null, null, false);

      assertResponseParserClassEquals(method, httpRequest, ParseJobListFromJsonResponse.class);
      assertSaxResponseParserClassEquals(method, null);
      assertExceptionParserClassEquals(method, null);

      checkFilters(httpRequest);
      httpRequest = Iterables.getOnlyElement(httpRequest.getFilters()).filter(httpRequest);

      assertRequestLineEquals(httpRequest, "GET https://api.gogrid.com/api/grid/job/get?v=1.5&"
            + "id=123&id=456&sig=3f446f171455fbb5574aecff4997b273&api_key=foo " + "HTTP/1.1");
      assertNonPayloadHeadersEqual(httpRequest, "");
      assertPayloadEquals(httpRequest, null, null, false);
   }

   @Override
   protected TypeLiteral<RestAnnotationProcessor<GridJobAsyncClient>> createTypeLiteral() {
      return new TypeLiteral<RestAnnotationProcessor<GridJobAsyncClient>>() {
      };
   }

}
