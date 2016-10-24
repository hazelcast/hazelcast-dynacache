package com.hazelcast.ibm.dynacache;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.logging.Logger;

public class Activator implements BundleActivator {
    private static Logger LOGGER = Logger.getLogger(Activator.class.getName());

    static {
        System.setProperty("hazelcast.osgi.start", "false");
    }

    public void start(BundleContext context) throws Exception {
        LOGGER.finest("hazelcast-dynacache bundle started");
    }

    public void stop(BundleContext context) throws Exception {
        LOGGER.finest("hazelcast-dynacache bundle stopped");
    }
}
