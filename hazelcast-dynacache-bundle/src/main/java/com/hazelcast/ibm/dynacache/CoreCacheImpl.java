package com.hazelcast.ibm.dynacache;

import com.hazelcast.client.osgi.HazelcastOSGiClientInstance;
import com.ibm.websphere.cache.CacheEntry;
import com.ibm.websphere.cache.EntryInfo;
import com.ibm.wsspi.cache.CacheStatistics;
import com.ibm.wsspi.cache.CoreCache;
import com.ibm.wsspi.cache.EventSource;

import java.util.Enumeration;
import java.util.Set;
import java.util.logging.Logger;

public class CoreCacheImpl implements CoreCache {
    private static Logger LOGGER = Logger.getLogger(CoreCacheImpl.class.getName());

    private final HazelcastOSGiClientInstance hazelcastInstance;

    String cacheName;

    public CoreCacheImpl(String cacheName, HazelcastOSGiClientInstance hazelcastInstance) {
        this.cacheName = cacheName;
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void clear() {
        LOGGER.finest("[CoreCache] clear");
        hazelcastInstance.getMap("liberty").clear();
    }

    @Override
    public boolean containsCacheId(Object cacheId) {
        LOGGER.finest("[CoreCache] containsCacheId");
        return hazelcastInstance.getMap("liberty").containsKey(cacheId);
    }

    @Override
    public CacheEntry get(Object cacheId) {
        LOGGER.finest("[CoreCache] get: " + cacheId);
        return (CacheEntry) hazelcastInstance.getMap("liberty").get(cacheId);

    }

    @Override
    public Set<Object> getCacheIds() {
        LOGGER.finest("[CoreCache] getCacheIds");
        return hazelcastInstance.getMap("liberty").keySet();
    }

    @Override
    public Set<Object> getCacheIds(Object keyword) {
        throw new UnsupportedOperationException();

    }

    @Override
    public String getCacheName() {
        LOGGER.finest("[CoreCache] getCacheName");
        return cacheName;
    }

    @Override
    public CacheStatistics getCacheStatistics() {
        LOGGER.finest("[CoreCache] getCacheStatistics");

        // throwing UnsupportedOperationException causes plugin to fail
        return null;
    }

    @Override
    public Set<Object> getDependencyIds() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Object> getTemplateIds() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void invalidate(Object id, boolean waitOnInvalidation) {
        LOGGER.finest("[CoreCache] invalidate: " + id + " " + waitOnInvalidation);
        hazelcastInstance.getMap("liberty").remove(id);
        for (Object key : hazelcastInstance.getMap("liberty").keySet()) {
            LOGGER.finest("Key: " + key);
            CacheEntryImpl entry = (CacheEntryImpl) hazelcastInstance.getMap("liberty").get(key);
            if (entry.dependencyIds.contains(id)) {
                hazelcastInstance.getMap("liberty").remove(key);
            }
        }
    }

    @Override
    public void invalidateByCacheId(Object cacheId, boolean waitOnInvalidation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void invalidateByDependency(Object dependency, boolean waitOnInvalidation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void invalidateByTemplate(String template, boolean waitOnInvalidation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CacheEntry put(EntryInfo ei, Object value) {
        LOGGER.finest("[CoreCache] put: " + ei.getIdObject());
        LOGGER.finest("[CoreCache] value: " + value);
        CacheEntryImpl entry = new CacheEntryImpl(value, ei);
        Enumeration dataIds = ei.getDataIds();
        if (dataIds.hasMoreElements()) {
            entry.dependencyIds.add(dataIds.nextElement());
        }
        hazelcastInstance.getMap("liberty").put(ei.getIdObject(), entry);
        hazelcastInstance.getMap("liberty").get(ei.getIdObject());
        return entry;
    }

    @Override
    public void refreshEntry(Object cacheId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEventSource(EventSource eventSource) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void start() {
        LOGGER.finest("[CoreCache] start");
    }

    @Override
    public void stop() {
        LOGGER.finest("[CoreCache] stop");
    }

    @Override
    public void touch(Object id, long validatorExpirationTime, long expirationTime) {
        throw new UnsupportedOperationException();
    }
}
