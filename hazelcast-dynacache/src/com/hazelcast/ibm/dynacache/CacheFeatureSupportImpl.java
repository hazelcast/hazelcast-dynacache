package com.hazelcast.ibm.dynacache;

import com.hazelcast.client.osgi.HazelcastClientOSGiService;
import com.ibm.wsspi.cache.CacheFeatureSupport;


public class CacheFeatureSupportImpl extends CacheFeatureSupport {

	
	@Override
	public boolean isAliasSupported() {
			
		// TODO Auto-generated method stub
		System.out.println("[CacheFeatureSupport] isAliasSupported");

		return false;
	}

	@Override
	public boolean isDiskCacheSupported() {
		System.out.println("[CacheFeatureSupport] isDiskCacheSupported");

		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isReplicationSupported() {
		// TODO Auto-generated method stub
		System.out.println("[CacheFeatureSupport] isReplicationSupported");

		return false;
	}

}
