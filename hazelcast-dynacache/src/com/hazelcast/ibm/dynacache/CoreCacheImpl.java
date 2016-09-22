package com.hazelcast.ibm.dynacache;

import static com.hazelcast.ibm.dynacache.Activator.hazelcastOSGiInstance;

import java.util.Enumeration;
import java.util.Set;

import com.ibm.websphere.cache.Cache;
import com.ibm.websphere.cache.CacheEntry;
import com.ibm.websphere.cache.EntryInfo;
import com.ibm.wsspi.cache.CacheStatistics;
import com.ibm.wsspi.cache.CoreCache;
import com.ibm.wsspi.cache.EventSource;

public class CoreCacheImpl implements CoreCache{

	String cacheName;
	public CoreCacheImpl(String cacheName) {
		this.cacheName = cacheName;
	}

	@Override
	public void clear() {
		System.out.println("[CoreCache] clear");
		hazelcastOSGiInstance.getMap("liberty").clear();
		
	}

	@Override
	public boolean containsCacheId(Object arg0) {
		System.out.println("[CoreCache] containsCacheId");
		return hazelcastOSGiInstance.getMap("liberty").containsKey(arg0);
	}

	@Override
	public CacheEntry get(Object arg0) {
		System.out.println("[CoreCache] get: " + arg0);
		return (CacheEntry)hazelcastOSGiInstance.getMap("liberty").get(arg0);
		
	}

	@Override
	public Set<Object> getCacheIds() {
		System.out.println("[CoreCache] getCacheIds");
		return hazelcastOSGiInstance.getMap("liberty").keySet();
	}

	@Override
	public Set<Object> getCacheIds(Object arg0) {
		// TODO Auto-generated method stub
		System.out.println("[CoreCache] getCacheIds<arg0>");

		return null;
	}

	@Override
	public String getCacheName() {
		System.out.println("[CoreCache] getCacheName");
		return cacheName;
	}

	@Override
	public CacheStatistics getCacheStatistics() {
		// TODO Auto-generated method stub
		System.out.println("[CoreCache] getCacheStatistics");
		return null;
	}

	@Override
	public Set<Object> getDependencyIds() {
		System.out.println("[CoreCache] getDependencyIds");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Object> getTemplateIds() {
		System.out.println("[CoreCache] getTemplateIds");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invalidate(Object arg0, boolean arg1) {
		// TODO Auto-generated method stub
		System.out.println("[CoreCache] invalidate: " + arg0 + " " + arg1);
		hazelcastOSGiInstance.getMap("liberty").remove(arg0);
		for(Object key : hazelcastOSGiInstance.getMap("liberty").keySet()){
			System.out.println(key);
			CacheEntryImp entry = (CacheEntryImp)hazelcastOSGiInstance.getMap("liberty").get(key);
			if(entry.dependencyIds.contains(arg0)){
				hazelcastOSGiInstance.getMap("liberty").remove(key);
			}
			}			
	}

	@Override
	public void invalidateByCacheId(Object arg0, boolean arg1) {
		// TODO Auto-generated method stub
		System.out.println("[CoreCache] invalidateByCacheId");

		
	}

	@Override
	public void invalidateByDependency(Object arg0, boolean arg1) {
		// TODO Auto-generated method stub
		System.out.println("[CoreCache] invalidateByDependency");

	}

	@Override
	public void invalidateByTemplate(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		System.out.println("[CoreCache] invalidateByTemplate");

	}

	@Override
	public CacheEntry put(EntryInfo arg0, Object arg1) {
		
		// TODO Auto-generated method stub
		System.out.println("[CoreCache] put: " + arg0.getIdObject());
		System.out.println("[CoreCache] value: " + arg1);
		CacheEntryImp entry = new CacheEntryImp(arg1,arg0);
//		System.out.println();
		Enumeration dataIds = arg0.getDataIds();
		if (dataIds.hasMoreElements()){
			entry.dependencyIds.add(dataIds.nextElement());
		}
		hazelcastOSGiInstance.getMap("liberty").put(arg0.getIdObject(), entry);
		hazelcastOSGiInstance.getMap("liberty").get(arg0.getIdObject());
		return null;
	}

	@Override
	public void refreshEntry(Object arg0) {
		System.out.println("[CoreCache] refreshEntry");

		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEventSource(EventSource arg0) {
		System.out.println("[CoreCache] setEventSource");

		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		System.out.println("[CoreCache] start");
	}

	@Override
	public void stop() {
		System.out.println("[CoreCache] stop");
		
	}

	@Override
	public void touch(Object arg0, long arg1, long arg2) {
		System.out.println("[CoreCache] touch");
		
	}

}
