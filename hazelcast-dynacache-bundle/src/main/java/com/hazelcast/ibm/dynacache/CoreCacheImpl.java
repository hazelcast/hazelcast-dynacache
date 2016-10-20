package com.hazelcast.ibm.dynacache;

import com.hazelcast.client.osgi.HazelcastOSGiClientInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiMap;
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

    private final String cacheName;
    private final IMap<Object, Object> cache;
    private final MultiMap<Object, Object> dependencyIds;

    public CoreCacheImpl(String cacheName, HazelcastOSGiClientInstance hazelcastInstance) {
        this.cacheName = cacheName;
        this.cache = hazelcastInstance.getMap(cacheName);
        this.dependencyIds = hazelcastInstance.getMultiMap(cacheName);
    }

    @Override
    public void clear() {
        LOGGER.finest("[CoreCache] clear");

        cache.clear();
        dependencyIds.clear();
    }

    @Override
    public boolean containsCacheId(Object cacheId) {
        LOGGER.finest("[CoreCache] containsCacheId");

        return cache.containsKey(cacheId);
    }

    @Override
    public CacheEntry get(Object cacheId) {
        LOGGER.finest("[CoreCache] get: " + cacheId);

        return (CacheEntry) cache.get(cacheId);
    }

    @Override
    public Set<Object> getCacheIds() {
        LOGGER.finest("[CoreCache] getCacheIds");

        return cache.keySet();
    }

    @Override
    public Set<Object> getCacheIds(Object keyword) {
        LOGGER.finest("[CoreCache] getCacheIds(keyword)");

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
        LOGGER.finest("[CoreCache] getDependencyIds");

        return dependencyIds.keySet();
    }

    @Override
    public Set<Object> getTemplateIds() {
        LOGGER.finest("[CoreCache] getTemplateIds");

        throw new UnsupportedOperationException();
    }

    @Override
    public void invalidate(Object id, boolean waitOnInvalidation) {
        LOGGER.finest("[CoreCache] invalidate: " + id + " " + waitOnInvalidation);

        invalidate(id);
    }

    @Override
    public void invalidateByCacheId(Object cacheId, boolean waitOnInvalidation) {
        LOGGER.finest("[CoreCache] invalidate(cacheId): " + cacheId + ":" + waitOnInvalidation);

        invalidate(cacheId);
    }

    private void invalidate(Object id) {
        CacheEntry removedEntry = (CacheEntry) cache.remove(id);

        Enumeration dataIds = removedEntry.getDataIds();
        while (dataIds.hasMoreElements()) {
            Object dependencyId = dataIds.nextElement();
            dependencyIds.remove(dependencyId, id);
        }
    }

    @Override
    public void invalidateByDependency(Object dependency, boolean waitOnInvalidation) {
        LOGGER.finest("[CoreCache] invalidateByDependency");

        Set<Object> ids = (Set<Object>) dependencyIds.remove(dependency);
        cache.executeOnKeys(ids, new DeleteEntryProcessor());
    }

    @Override
    public void invalidateByTemplate(String template, boolean waitOnInvalidation) {
        LOGGER.finest("[CoreCache] invalidateByTemplate");

        throw new UnsupportedOperationException();
    }

    @Override
    public CacheEntry put(EntryInfo ei, Object value) {
        trace(ei, value);

        CacheEntryImpl cacheEntry = new CacheEntryImpl(value, ei);

        String id = ei.getId();

        cache.put(id, cacheEntry);

        Enumeration dataIds = ei.getDataIds();
        while (dataIds.hasMoreElements()) {
            Object dependencyId = dataIds.nextElement();
            dependencyIds.put(dependencyId, id);
        }

        return cacheEntry;
    }

    @Override
    public void refreshEntry(Object cacheId) {
        LOGGER.finest("[CoreCache] refreshEntry");
    }

    @Override
    public void setEventSource(EventSource eventSource) {
        LOGGER.finest("[CoreCache] setEventSource");

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
        LOGGER.finest("[CoreCache] touch");

        throw new UnsupportedOperationException();
    }

    private void trace(EntryInfo ei, Object value) {
        LOGGER.finest("[CoreCache] PUT METHOD TRACE:");
        LOGGER.finest("--------------------------------------------------------------------------");
        LOGGER.finest("Value: " + value);
        LOGGER.finest("EntryInfo.getId(): " + ei.getId());
        LOGGER.finest("EntryInfo.getIdObject(): " + ei.getIdObject());
        LOGGER.finest("EntryInfo.getTemplate(): " + ei.getTemplate());
        LOGGER.finest("EntryInfo.getUserMetaData(): " + ei.getUserMetaData());

        if (ei.getDataIds() != null) {
            Enumeration dataIds = ei.getDataIds();
            while (dataIds.hasMoreElements()) {
                Object dataId = dataIds.nextElement();
                LOGGER.finest("EntryInfo.getDataIds element: " + dataId);
            }
        }

        if (ei.getAliasList() != null) {
            Enumeration aliasList = ei.getAliasList();
            while (aliasList.hasMoreElements()) {
                Object alias = aliasList.nextElement();
                LOGGER.finest("EntryInfo.getAliastList element: " + alias);
            }
        }

        if (ei.getTemplates() != null) {
            Enumeration templates = ei.getTemplates();
            while (templates.hasMoreElements()) {
                Object template = templates.nextElement();
                LOGGER.finest("EntryInfo.getTemplates element: " + template);
            }
        }
        LOGGER.finest("--------------------------------------------------------------------------");
    }
}
