/*
 * Copyright (c) 2008-2016, Hazelcast, Inc. All Rights Reserved.
 *
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
 */

package com.hazelcast.client.osgi;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.osgi.HazelcastOSGiInstance;
import org.osgi.framework.Bundle;

import java.util.Set;

/**
 * Contract point for Hazelcast services on top of OSGI.
 */
public interface HazelcastClientOSGiService {

    /**
     * System property for starting a default Hazelcast instance per Hazelcast OSGI bundle.
     */
    String HAZELCAST_OSGI_START = "hazelcast.osgi.start";

    /**
     * System property for disabling the behaviour that registers created Hazelcast instances as OSGI service.
     */
    String HAZELCAST_OSGI_REGISTER_DISABLED = "hazelcast.osgi.register.disabled";

    /**
     * System property for disabling the behaviour that gives different group name
     * to each Hazelcast bundle.
     */
    String HAZELCAST_OSGI_GROUPING_DISABLED = "hazelcast.osgi.grouping.disabled";

    /**
     * Gets the id of service.
     *
     * @return the id of service
     */
    String getId();

    /**
     * Gets the owner {@link Bundle} of this instance.
     *
     * @return the owner {@link Bundle} of this instance
     */
    Bundle getOwnerBundle();

    /**
     * Gets the default {@link HazelcastOSGiClientInstance}.
     *
     * @return the default {@link HazelcastOSGiClientInstance}
     */
    HazelcastOSGiClientInstance getDefaultHazelcastInstance();

    /**
     * Creates a new {@link HazelcastOSGiClientInstance}
     * on the owner bundle with specified configuration.
     *
     * @param config Configuration for the new {@link HazelcastOSGiClientInstance} (member)
     * @return the new {@link HazelcastOSGiClientInstance}
     */
    HazelcastOSGiClientInstance newHazelcastInstance(ClientConfig config);

    /**
     * Creates a new {@link HazelcastOSGiClientInstance}
     * on the owner bundle with default configuration.
     *
     * @return the new {@link HazelcastOSGiClientInstance}
     */
    HazelcastOSGiClientInstance newHazelcastInstance();

    /**
     * Gets an existing {@link HazelcastOSGiInstance} with its <code>instanceName</code>.
     *
     * @param instanceName Name of the {@link HazelcastOSGiClientInstance} (member)
     * @return an existing {@link HazelcastOSGiClientInstance}
     */
    HazelcastOSGiClientInstance getHazelcastInstanceByName(String instanceName);

    /**
     * Gets all active/running {@link HazelcastOSGiClientInstance}s on the owner bundle.
     *
     * @return all active/running {@link HazelcastOSGiClientInstance}s on the owner bundle
     */
    Set<HazelcastOSGiClientInstance> getAllHazelcastInstances();

    /**
     * Shuts down the given {@link HazelcastOSGiInstance} on the owner bundle.
     *
     * @param instance the {@link HazelcastOSGiClientInstance} to shutdown
     */
    void shutdownHazelcastInstance(HazelcastOSGiClientInstance instance);

    /**
     * Shuts down all running {@link HazelcastOSGiClientInstance}s on the owner bundle.
     */
    void shutdownAll();

}
