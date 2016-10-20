package com.hazelcast.ibm.dynacache;

import com.ibm.wsspi.cache.CacheFeatureSupport;

import java.util.logging.Logger;

public class CacheFeatureSupportImpl extends CacheFeatureSupport {
    private static Logger LOGGER = Logger.getLogger(CacheFeatureSupportImpl.class.getName());

    @Override
    public boolean isAliasSupported() {
        LOGGER.finest("[CacheFeatureSupport] isAliasSupported");

        return false;
    }

    @Override
    public boolean isDiskCacheSupported() {
        LOGGER.finest("[CacheFeatureSupport] isDiskCacheSupported");

        return false;
    }

    @Override
    public boolean isReplicationSupported() {
        LOGGER.finest("[CacheFeatureSupport] isReplicationSupported");

        return true;
    }
}
