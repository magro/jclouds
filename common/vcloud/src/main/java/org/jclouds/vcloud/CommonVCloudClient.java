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
package org.jclouds.vcloud;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.jclouds.concurrent.Timeout;
import org.jclouds.vcloud.domain.Catalog;
import org.jclouds.vcloud.domain.CatalogItem;
import org.jclouds.vcloud.domain.Org;
import org.jclouds.vcloud.domain.Task;
import org.jclouds.vcloud.domain.TasksList;
import org.jclouds.vcloud.domain.VDC;
import org.jclouds.vcloud.domain.network.OrgNetwork;

/**
 * Provides access to VCloud resources via their REST API.
 * <p/>
 * 
 * @see <a href="http://communities.vmware.com/community/developer/forums/vcloudapi" />
 * @author Adrian Cole
 */
@Timeout(duration = 300, timeUnit = TimeUnit.SECONDS)
public interface CommonVCloudClient {

   Org getOrg(URI orgId);

   /**
    * This call returns a list of all vCloud Data Centers (vdcs), catalogs, and task lists within
    * the organization.
    * 
    * @param name
    *           organization name, or null for the default
    * @throws NoSuchElementException
    *            if you specified an org name that isn't present
    */
   Org findOrgNamed(@Nullable String name);

   Catalog getCatalog(URI catalogId);

   /**
    * returns the catalog in the organization associated with the specified name. Note that both
    * parameters can be null to choose default.
    * 
    * @param orgName
    *           organization name, or null for the default
    * @param catalogName
    *           catalog name, or null for the default
    * @throws NoSuchElementException
    *            if you specified an org or catalog name that isn't present
    */
   Catalog findCatalogInOrgNamed(@Nullable String orgName, @Nullable String catalogName);

   CatalogItem getCatalogItem(URI catalogItem);

   /**
    * returns the catalog item in the catalog associated with the specified name. Note that the org
    * and catalog parameters can be null to choose default.
    * 
    * @param orgName
    *           organization name, or null for the default
    * @param catalogName
    *           catalog name, or null for the default
    * @param itemName
    *           item you wish to lookup
    * 
    * @throws NoSuchElementException
    *            if you specified an org, catalog, or catalog item name that isn't present
    */
   CatalogItem findCatalogItemInOrgCatalogNamed(@Nullable String orgName, @Nullable String catalogName, String itemName);

   OrgNetwork findNetworkInOrgVDCNamed(@Nullable String orgName, @Nullable String catalogName, String networkName);

   OrgNetwork getNetwork(URI network);

   VDC getVDC(URI vdc);

   /**
    * returns the VDC in the organization associated with the specified name. Note that both
    * parameters can be null to choose default.
    * 
    * @param orgName
    *           organization name, or null for the default
    * @param vdcName
    *           catalog name, or null for the default
    * @throws NoSuchElementException
    *            if you specified an org or vdc name that isn't present
    */
   VDC findVDCInOrgNamed(String orgName, String vdcName);

   TasksList getTasksList(URI tasksListId);

   TasksList findTasksListInOrgNamed(String orgName);

   /**
    * Whenever the result of a request cannot be returned immediately, the server creates a Task
    * object and includes it in the response, as a member of the Tasks container in the response
    * body. Each Task has an href value, which is a URL that the client can use to retrieve the Task
    * element alone, without the rest of the response in which it was contained. All information
    * about the task is included in the Task element when it is returned in the response’s Tasks
    * container, so a client does not need to make an additional request to the Task URL unless it
    * wants to follow the progress of a task that was incomplete.
    */
   Task getTask(URI taskId);

   void cancelTask(URI taskId);

}
