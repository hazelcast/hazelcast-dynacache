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

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;
import com.hazelcast.osgi.HazelcastOSGiService;
import com.hazelcast.osgi.impl.HazelcastInternalOSGiService;
import com.hazelcast.util.ExceptionUtil;
import com.hazelcast.util.StringUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implementation of {@link HazelcastInternalOSGiService}.
 *
 * @see HazelcastInternalOSGiService
 * @see HazelcastOSGiService
 */
class HazelcastClientOSGiServiceImpl
        implements HazelcastClientInternalOSGiService {

    private static final ILogger LOGGER = Logger.getLogger(HazelcastOSGiService.class);

    private final Object serviceMutex = new Object();

    private final Bundle ownerBundle;
    private final BundleContext ownerBundleContext;
    private final String id;
    private final ConcurrentMap<HazelcastOSGiClientInstance, ServiceRegistration> instanceServiceRegistrationMap
            = new ConcurrentHashMap<HazelcastOSGiClientInstance, ServiceRegistration>();
    private final ConcurrentMap<String, HazelcastOSGiClientInstance> instanceMap
            = new ConcurrentHashMap<String, HazelcastOSGiClientInstance>();

    // No need to "volatile" since this field is guarded and happens-before is handled by
    // `synchronized` blocks of `serviceMutex` instance.
    private ServiceRegistration serviceRegistration;

    // Defined as `volatile` because even though this field is written inside `synchronized` blocks,
    // it can be read from any thread without `synchronized` blocks and without `volatile` they may seen invalid value.
    private volatile HazelcastOSGiClientInstance hazelcastInstance;


    public HazelcastClientOSGiServiceImpl(Bundle ownerBundle) {
        this(ownerBundle, DEFAULT_ID);
    }

    public HazelcastClientOSGiServiceImpl(Bundle ownerBundle, String id) {
        this.ownerBundle = ownerBundle;
        this.ownerBundleContext = ownerBundle.getBundleContext();
        this.id = id;
    }

    private void checkActive() {
        if (!isActive()) {
            throw new IllegalStateException("Hazelcast OSGI Service is not active!");
        }
    }

    private boolean shouldSetGroupName(GroupConfig groupConfig) {
        if (groupConfig == null
                || StringUtil.isNullOrEmpty(groupConfig.getName())
                || GroupConfig.DEFAULT_GROUP_NAME.equals(groupConfig.getName())) {
            if (!Boolean.getBoolean(HAZELCAST_OSGI_GROUPING_DISABLED)) {
                return true;
            }
        }
        return false;
    }

    private Config getConfig(Config config) {
        if (config == null) {
            config = new XmlConfigBuilder().build();
        }
        GroupConfig groupConfig = config.getGroupConfig();
        if (shouldSetGroupName(groupConfig)) {
            String groupName = id;
            if (groupConfig == null) {
                config.setGroupConfig(new GroupConfig(groupName));
            } else {
                groupConfig.setName(groupName);
            }
        }
        return config;
    }

    private HazelcastInstance createHazelcastInstance(ClientConfig config) {
        return HazelcastClient.newHazelcastClient(config);
    }

    private HazelcastOSGiClientInstance registerInstance(HazelcastInstance instance) {
        HazelcastOSGiClientInstance hazelcastOSGiClientInstance;
        if (instance instanceof HazelcastOSGiClientInstance) {
            hazelcastOSGiClientInstance = (HazelcastOSGiClientInstance) instance;
        } else {
            hazelcastOSGiClientInstance = new HazelcastOSGiClientInstanceImpl(instance, this);
        }
        if (!Boolean.getBoolean(HAZELCAST_OSGI_REGISTER_DISABLED)) {
            ServiceRegistration serviceRegistration =
                    ownerBundleContext.registerService(HazelcastInstance.class.getName(), hazelcastOSGiClientInstance, null);
            instanceServiceRegistrationMap.put(hazelcastOSGiClientInstance, serviceRegistration);
        }
        instanceMap.put(instance.getName(), hazelcastOSGiClientInstance);
        return hazelcastOSGiClientInstance;
    }

    private void deregisterInstance(HazelcastOSGiClientInstance hazelcastOSGiInstance) {
        instanceMap.remove(hazelcastOSGiInstance.getName());
        ServiceRegistration serviceRegistration =
                instanceServiceRegistrationMap.remove(hazelcastOSGiInstance);
        if (serviceRegistration != null) {
            ownerBundleContext.ungetService(serviceRegistration.getReference());
            serviceRegistration.unregister();
        }
    }

    private void shutdownDefaultHazelcastInstanceIfActive() {
        if (hazelcastInstance != null) {
            shutdownHazelcastInstanceInternalSafely(hazelcastInstance);
            hazelcastInstance = null;
        }
    }

    private void shutdownAllInternal() {
        for (HazelcastOSGiClientInstance instance : instanceMap.values()) {
            shutdownHazelcastInstanceInternalSafely(instance);
        }
        // Default instance may not be registered due to set `HAZELCAST_OSGI_REGISTER_DISABLED` flag,
        // So be sure that it is shutdown.
        shutdownDefaultHazelcastInstanceIfActive();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Bundle getOwnerBundle() {
        return ownerBundle;
    }

    @Override
    public boolean isActive() {
        return ownerBundle.getState() == Bundle.ACTIVE;
    }

    @Override
    public void activate() {
        // No need to complex lock-free approaches since this is not called frequently. Simple is good.
        synchronized (serviceMutex) {
            if (ownerBundle.getState() == Bundle.STARTING) {
                try {
                    if (hazelcastInstance != null) {
                        System.out.println("Default Hazelcast instance should be null while activating service !");
                        shutdownDefaultHazelcastInstanceIfActive();
                    }
                    if (Boolean.getBoolean(HAZELCAST_OSGI_START)) {
                        hazelcastInstance =
                                new HazelcastOSGiClientInstanceImpl(createHazelcastInstance(null), this);
                        System.out.println("Default Hazelcast instance has been created");
                    }
                    if (hazelcastInstance != null && !Boolean.getBoolean(HAZELCAST_OSGI_REGISTER_DISABLED)) {
                        registerInstance(hazelcastInstance);
                        System.out.println("Default Hazelcast instance has been registered as OSGI service");
                    }
                    serviceRegistration =
                            ownerBundleContext.registerService(HazelcastClientOSGiService.class.getName(), this, null);
                    System.out.println(this + " has been registered as OSGI service and activated now");
                } catch (Throwable t) {
                    // If somehow default instance is activated, revert and deactivate it.
                    shutdownDefaultHazelcastInstanceIfActive();
                    ExceptionUtil.rethrow(t);
                }
            }
        }
    }

    @Override
    public void deactivate() {
        // No need to complex lock-free approaches since this is not called frequently. Simple is good.
        synchronized (serviceMutex) {
            if (ownerBundle.getState() == Bundle.STOPPING) {
                try {
                    shutdownAllInternal();
                    try {
                        ownerBundleContext.ungetService(serviceRegistration.getReference());
                        serviceRegistration.unregister();
                    } catch (Throwable t) {
                        LOGGER.finest("Error occurred while deregistering " + this, t);
                    }
                    LOGGER.info(this + " has been deregistered as OSGI service and deactivated");
                } finally {
                    serviceRegistration = null;
                }
            }
        }
    }

    @Override
    public HazelcastOSGiClientInstance getDefaultHazelcastInstance() {
        checkActive();

        // No need to synchronization since this is not a mutating operation on instances.
        // If at this time service is deactivated, this method just returns terminated instance.

        return hazelcastInstance;
    }

    @Override
    public HazelcastOSGiClientInstance newHazelcastInstance(ClientConfig config) {
        // No need to complex lock-free approaches since this is not called frequently. Simple is good.
        synchronized (serviceMutex) {
            checkActive();

            return registerInstance(createHazelcastInstance(config));
        }
    }

    @Override
    public HazelcastOSGiClientInstance newHazelcastInstance() {
        // No need to complex lock-free approaches since this is not called frequently. Simple is good.
        synchronized (serviceMutex) {
            checkActive();

            return registerInstance(createHazelcastInstance(null));
        }
    }

    @Override
    public HazelcastOSGiClientInstance getHazelcastInstanceByName(String instanceName) {
        checkActive();

        // No need to synchronization since this is not a mutating operation on instances.
        // If at this time service is deactivated, this method just returns terminated instance.

        return instanceMap.get(instanceName);
    }

    @Override
    public Set<HazelcastOSGiClientInstance> getAllHazelcastInstances() {
        checkActive();

        // No need to synchronization since this is not a mutating operation on instances.
        // If at this time service is deactivated, this method just returns terminated instances.

        return new HashSet<HazelcastOSGiClientInstance>(instanceMap.values());
    }

    @Override
    public void shutdownHazelcastInstance(HazelcastOSGiClientInstance instance) {
        // No need to complex lock-free approaches since this is not called frequently. Simple is good.
        synchronized (serviceMutex) {
            checkActive();

            shutdownHazelcastInstanceInternal(instance);
        }
    }

    private void shutdownHazelcastInstanceInternal(HazelcastOSGiClientInstance instance) {
        try {
            deregisterInstance(instance);
        } catch (Throwable t) {
            LOGGER.finest("Error occurred while deregistering " + instance, t);
        }
        instance.shutdown();
    }

    private void shutdownHazelcastInstanceInternalSafely(HazelcastOSGiClientInstance instance) {
        try {
            shutdownHazelcastInstanceInternal(instance);
        } catch (Throwable t) {
            LOGGER.finest("Error occurred while shutting down " + instance, t);
        }
    }

    @Override
    public void shutdownAll() {
        // No need to complex lock-free approaches since this is not called frequently. Simple is good.
        synchronized (serviceMutex) {
            checkActive();

            shutdownAllInternal();
        }
    }

    @Override
    public String toString() {
        return "HazelcastClientOSGiServiceImpl{"
                + "ownerBundle=" + ownerBundle
                + ", hazelcastInstance=" + hazelcastInstance
                + ", active=" + isActive()
                + ", id=" + id
                + '}';
    }

}
