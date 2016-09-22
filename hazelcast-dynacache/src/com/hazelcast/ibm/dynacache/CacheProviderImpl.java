package com.hazelcast.ibm.dynacache;

import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;

import com.hazelcast.client.osgi.HazelcastClientOSGiService;
import com.hazelcast.client.osgi.HazelcastOSGiClientInstance;
import com.hazelcast.client.config.*;
import com.ibm.wsspi.cache.CacheConfig;
import com.ibm.wsspi.cache.CacheFeatureSupport;
import com.ibm.wsspi.cache.CacheProvider;
import com.ibm.wsspi.cache.CoreCache;
import static com.hazelcast.ibm.dynacache.Activator.hazelcastOSGiInstance;
import static com.hazelcast.ibm.dynacache.Activator.hazelcastClientOSGiService;

import java.util.Enumeration;
import java.util.Properties;

@Component(service = {CacheProvider.class},property = {"name=hazelcast", "service.vendor=IBM"})
public class CacheProviderImpl  implements CacheProvider {

	
//	CacheProviderImpl(){
//		System.out.println("empty constructor");
//	}
	@Override
	public CoreCache createCache(CacheConfig arg0) {
		
		// TODO Auto-generated method stub
		System.out.println(arg0.getCacheName());
		System.out.println(arg0.getMaxCacheSize());
		System.out.println(arg0.getProperties());
		
		System.out.println("[CacheProvider] Create Cache");
		ClientConfig config = new ClientConfig();
		config.setClassLoader(this.getClass().getClassLoader());
		hazelcastOSGiInstance = hazelcastClientOSGiService.newHazelcastInstance(config);
		return new CoreCacheImpl(arg0.getCacheName());
	}

	@Override
	public CacheFeatureSupport getCacheFeatureSupport() {
		// TODO Auto-generated method stub
		System.out.println("[CacheProvider] getCacheFeatureSupport");
		return new CacheFeatureSupportImpl();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		System.out.println("[CacheProvider] getName");
		return "hazelcast";
	}

	@Override
	public void start() {
		System.out.println("[CacheProvider] start");
	}

	@Override
	public void stop() {
		System.out.println("[CacheProvider] stop");

		// TODO Auto-generated method stub
		
	}

}
