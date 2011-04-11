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
package org.jclouds.azurequeue.xml;

import static org.testng.Assert.assertEquals;

import java.io.InputStream;
import java.util.Set;

import org.jclouds.azurequeue.domain.QueueMessage;
import org.jclouds.date.DateService;
import org.jclouds.http.functions.BaseHandlerTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;

/**
 * Tests behavior of {@code QueueMessagesListHandler}
 * 
 * @author Adrian Cole
 */
// NOTE:without testName, this will not call @Before* and fail w/NPE during surefire
@Test(groups = "unit", testName = "QueueMessagesListHandlerTest")
public class QueueMessagesListHandlerTest extends BaseHandlerTest {
   private DateService dateService;

   @BeforeTest
   protected void setUpInjector() {
      super.setUpInjector();
      dateService = injector.getInstance(DateService.class);
   }

   public void testApplyInputStream() {
      InputStream is = getClass().getResourceAsStream("/test_get_messages.xml");
      Set<QueueMessage> expected = ImmutableSet.<QueueMessage> of(

      new QueueMessage("43190737-06f4-4ccf-b600-28f410707df3", dateService
               .rfc822DateParse("Fri, 11 Jun 2010 18:35:08 GMT"), dateService
               .rfc822DateParse("Fri, 11 Jun 2010 18:35:13 GMT"), 1,
               "AgAAAAEAAADZcwAADlwO5JQJywE=", dateService
                        .rfc822DateParse("Fri, 11 Jun 2010 18:35:39 GMT"), "holycow"),
               new QueueMessage("7b75a124-7efe-45a2-97e4-388664319718", dateService
                        .rfc822DateParse("Fri, 11 Jun 2010 18:35:09 GMT"), dateService
                        .rfc822DateParse("Fri, 11 Jun 2010 18:35:13 GMT"), 1,
                        "AgAAAAEAAADZcwAADlwO5JQJywE=", dateService
                                 .rfc822DateParse("Fri, 11 Jun 2010 18:35:39 GMT"), "holymoo"));

      Set<QueueMessage> result = factory.create(
               injector.getInstance(QueueMessagesListHandler.class)).parse(is);
      assertEquals(result, expected);
   }
}
