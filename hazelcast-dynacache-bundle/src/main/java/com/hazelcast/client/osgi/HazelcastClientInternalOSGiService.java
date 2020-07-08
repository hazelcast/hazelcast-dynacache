/*
 * Copyright 2020 Hazelcast Inc.
 *
 * Licensed under the Hazelcast Community License (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://hazelcast.com/hazelcast-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.hazelcast.client.osgi;

import com.hazelcast.instance.BuildInfoProvider;
import com.hazelcast.osgi.HazelcastOSGiService;

/**
 * Contract point for internal Hazelcast services on top of OSGI.
 *
 * @see HazelcastOSGiService
 */
public interface HazelcastClientInternalOSGiService
        extends HazelcastClientOSGiService {

    /**
     * Default id for {@link HazelcastClientInternalOSGiService} instance
     */
    String DEFAULT_ID =
            BuildInfoProvider.getBuildInfo().getVersion()
            + "#"
            + (BuildInfoProvider.getBuildInfo().isEnterprise() ? "EE" : "OSS");

    /**
     * Default group name to be used when grouping is not disabled with
     * {@link HazelcastOSGiService#HAZELCAST_OSGI_GROUPING_DISABLED}.
     */
    String DEFAULT_CLUSTER_NAME = DEFAULT_ID;

    /**
     * Returns the state of the service about if it is active or not.
     *
     * @return <code>true</code> if the service is active, otherwise <code>false</code>
     */
    boolean isActive();

    /**
     * Activates the service.
     */
    void activate();

    /**
     * Deactivates the service.
     */
    void deactivate();

}
