package com.hazelcast.ibm.dynacache;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.client.osgi.HazelcastClientOSGiService;
import com.hazelcast.client.osgi.HazelcastOSGiClientInstance;
import com.ibm.wsspi.cache.CacheConfig;
import com.ibm.wsspi.cache.CacheFeatureSupport;
import com.ibm.wsspi.cache.CacheProvider;
import com.ibm.wsspi.cache.CoreCache;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class CacheProviderImpl implements CacheProvider {

    private static Logger LOGGER = Logger.getLogger(CacheProviderImpl.class.getName());

    private HazelcastClientOSGiService hazelcastClientOSGIService;

    @SuppressWarnings("unused")
    public synchronized void setHazelcastClientOSGIService(HazelcastClientOSGiService hazelcastClientOSGIService) {
        LOGGER.finest("Setting hazelcastClientOSGIService");
        this.hazelcastClientOSGIService = hazelcastClientOSGIService;
    }

    @SuppressWarnings("unused")
    public synchronized void unsetHazelcastClientOSGIService(HazelcastClientOSGiService hazelcastClientOSGIService) {
        if (this.hazelcastClientOSGIService == hazelcastClientOSGIService) {
            this.hazelcastClientOSGIService = null;
        }
    }

    @Override
    public CoreCache createCache(CacheConfig cacheConfig) {
        LOGGER.finest("[CacheProvider] Create Cache");

        LOGGER.finest("CacheName:" + cacheConfig.getCacheName());
        LOGGER.finest("MaxCacheSize: " + cacheConfig.getMaxCacheSize());
        LOGGER.finest("<<<<< Printing out cacheConfig.getProperties() >>>>>");
        for (Map.Entry<String, String> entry : cacheConfig.getProperties().entrySet()) {
            LOGGER.finest(entry.getKey() + " : " + entry.getValue());
        }

        String clientConfigLocation = System.getProperty("hazelcast.client.config");

        LOGGER.finest("Hazelcast client config location: " + clientConfigLocation);

        ClientConfig config;
        if (clientConfigLocation != null) {
            try {
                XmlClientConfigBuilder xmlClientConfigBuilder =
                        new XmlClientConfigBuilder(clientConfigLocation);
                config = xmlClientConfigBuilder.build();

                LOGGER.finest("Configured Hazelcast client using config file at [" + clientConfigLocation + "]");
            } catch (IOException e) {
                throw new RuntimeException("Failed to configure Hazelcast client using config file at [" +
                        clientConfigLocation + "]", e);
            }
        } else {
            config = new ClientConfig();
        }

        config.setClassLoader(this.getClass().getClassLoader());

        LOGGER.finest("hazelcastClientOSGIService: " + hazelcastClientOSGIService);

        HazelcastOSGiClientInstance hazelcastClientInstance = hazelcastClientOSGIService.newHazelcastInstance(config);

        LOGGER.finest("HazelcastClientInstance: " + hazelcastClientInstance);

        return new CoreCacheImpl(cacheConfig.getCacheName(), hazelcastClientInstance);
    }

    @Override
    public CacheFeatureSupport getCacheFeatureSupport() {
        LOGGER.finest("[CacheProvider] getCacheFeatureSupport");
        return new CacheFeatureSupportImpl();
    }

    @Override
    public String getName() {
        LOGGER.finest("[CacheProvider] getName");
        return "hazelcast";
    }

    @Override
    public void start() {
        LOGGER.finest("[CacheProvider] start");
    }

    @Override
    public void stop() {
        LOGGER.finest("[CacheProvider] stop");
    }
}
