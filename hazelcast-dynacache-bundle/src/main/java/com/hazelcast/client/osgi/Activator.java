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

import java.lang.reflect.Method;

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
        // Try to start javax.scripting - JSR 223
        activateJavaxScripting(context);

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

    private void activateJavaxScripting(BundleContext context)
            throws Exception {
        if (isJavaxScriptingAvailable()) {
            Class<?> clazz = context.getBundle().loadClass("com.hazelcast.osgi.impl.ScriptEngineActivator");
            Method register = clazz.getDeclaredMethod("registerOsgiScriptEngineManager", BundleContext.class);
            register.setAccessible(true);
            register.invoke(clazz, context);
        } else {
            LOGGER.warning("Hazelcast: javax.scripting is not available, scripts from Management Center cannot be executed!");
        }
    }

    static boolean isJavaxScriptingAvailable() {
        try {
            Class.forName("javax.script.ScriptEngineManager");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
