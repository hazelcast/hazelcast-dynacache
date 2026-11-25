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

import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Hazelcast OSGi bundle activator.
 *
 * @see org.osgi.framework.BundleActivator
 */
public class Activator
        implements BundleActivator {

    private static final ILogger LOGGER = Logger.getLogger(Activator.class);

    // Defined as `volatile` since Javadoc of `BundleActivator` says that
    // `The Framework must not concurrently call a BundleActivator object`
    // and also it is marked as `@NotThreadSafe`.
    // So it can be called from different threads but cannot be called concurrently.
    private volatile HazelcastClientInternalOSGiService hazelcastClientOSGiService;

    @Override
    public void start(BundleContext context)
            throws Exception {
        assert hazelcastClientOSGiService == null : "Hazelcast OSGI service should be null while starting!";

        // Create a new Hazelcast OSGI service with given bundle and its context
        hazelcastClientOSGiService = new HazelcastClientOSGiServiceImpl(context.getBundle());

        // Activate new created Hazelcast OSGI service
        hazelcastClientOSGiService.activate();

        LOGGER.finest("hazelcast-dynacache bundle started");
    }

    @Override
    public void stop(BundleContext context)
            throws Exception {
        assert hazelcastClientOSGiService != null : "Hazelcast OSGI service should not be null while stopping!";

        hazelcastClientOSGiService.deactivate();
        hazelcastClientOSGiService = null;

        LOGGER.finest("hazelcast-dynacache bundle stopped");
    }

}
