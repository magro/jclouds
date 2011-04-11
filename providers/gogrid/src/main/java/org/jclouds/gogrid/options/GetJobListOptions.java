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
package org.jclouds.gogrid.options;

import org.jclouds.gogrid.domain.JobState;
import org.jclouds.gogrid.domain.ObjectType;
import org.jclouds.http.options.BaseHttpRequestOptions;

import java.util.Date;

import static com.google.common.base.Preconditions.checkState;
import static org.jclouds.gogrid.reference.GoGridQueryParams.*;

/**
 * @author Oleksiy Yarmula
 */
public class GetJobListOptions extends BaseHttpRequestOptions {

    public static final GetJobListOptions NONE = new GetJobListOptions();

    public GetJobListOptions maxItemsNumber(Integer maxNumber) {
        checkState(!queryParameters.containsKey(MAX_NUMBER_KEY), "Can't have duplicate parameter of max returned items");
        queryParameters.put(MAX_NUMBER_KEY, maxNumber.toString());
        return this;
    }

    public GetJobListOptions withStartDate(Date startDate) {
        checkState(!queryParameters.containsKey(START_DATE_KEY), "Can't have duplicate start date for filtering");
        queryParameters.put(START_DATE_KEY, String.valueOf(startDate.getTime()));
        return this;
    }

    public GetJobListOptions withEndDate(Date endDate) {
        checkState(!queryParameters.containsKey(END_DATE_KEY), "Can't have duplicate end date for filtering");
        queryParameters.put(END_DATE_KEY, String.valueOf(endDate.getTime()));
        return this;
    }

    public GetJobListOptions withOwner(String owner) {
        checkState(!queryParameters.containsKey(OWNER_KEY), "Can't have duplicate owner name for filtering");
        queryParameters.put(OWNER_KEY, owner);
        return this;
    }

    public GetJobListOptions onlyForState(JobState jobState) {
        checkState(!queryParameters.containsKey(JOB_STATE_KEY), "Can't have duplicate job state for filtering");
        queryParameters.put(JOB_STATE_KEY, jobState.toString());
        return this;
    }

    public GetJobListOptions onlyForObjectType(ObjectType objectType) {
        checkState(!queryParameters.containsKey(JOB_OBJECT_TYPE_KEY), "Can't have duplicate object type for filtering");
        queryParameters.put(JOB_OBJECT_TYPE_KEY, objectType.toString());
        return this;
    }

    public GetJobListOptions onlyForObjectName(String objectName) {
        checkState(!queryParameters.containsKey(OBJECT_KEY), "Can't have duplicate object name for filtering");
        queryParameters.put(OBJECT_KEY, objectName);
        return this;
    }

    /*
    * This method is intended for testing
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GetJobListOptions options = (GetJobListOptions) o;

        return buildQueryParameters().equals(options.buildQueryParameters());
    }

    public static class Builder {
        public GetJobListOptions create() {
             return new GetJobListOptions();
        }

        public GetJobListOptions latestJobForObjectByName(String serverName) {
            return new GetJobListOptions().
                    maxItemsNumber(1).
                    onlyForObjectName(serverName);
        }
    }

}
