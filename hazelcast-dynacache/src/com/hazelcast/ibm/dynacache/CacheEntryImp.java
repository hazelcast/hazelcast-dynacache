package com.hazelcast.ibm.dynacache;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashSet;

import com.ibm.websphere.cache.CacheEntry;
import com.ibm.websphere.cache.EntryInfo;

final public class CacheEntryImp  implements CacheEntry, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4457475859295532282L;
	/**
	 * 
	 */
	Object value;
	EntryInfo entryInfo;
	public HashSet<Object> dependencyIds = new HashSet<>();
	
	
//	public CacheEntryImp(Object ob,EntryInfo entryInfo) {
//		// TODO Auto-generated constructor stub
//		this.value = ob;
//		this.entryInfo = entryInfo;
//	}

	public CacheEntryImp(Object ob, EntryInfo ei) {
		// TODO Auto-generated constructor stub
		this.value = ob;
		this.entryInfo = ei;
	
	}

	@Override
	public void finish() {
		System.out.println("[CacheEntry] Finish");
		// TODO Auto-generated method stub
		
	}

	@Override
	public Enumeration getAliasList() {
		System.out.println("[CacheEntry] getAliasList");
		return entryInfo.getAliasList();
	}

	@Override
	public int getCacheType() {
		System.out.println("[CacheEntry] getCacheType");
		return entryInfo.getCacheType();
	}

	@Override
	public long getCacheValueSize() {
		System.out.println("[CacheEntry] getCacheValueSize");
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getCreationTime() {
		System.out.println("[CacheEntry] getCreationTime");
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Enumeration getDataIds() {
		System.out.println("[CacheEntry] getDataIds");
		// TODO Auto-generated method stub
		return entryInfo.getDataIds();
	}

	@Override
	public byte[] getDisplayValue() {
		System.out.println("[CacheEntry] getDisplayValue");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getExpirationTime() {
		System.out.println("[CacheEntry] getExpirationTime");
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getExternalCacheGroupId() {
		System.out.println("[CacheEntry] getExternalCacheGroupId");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		System.out.println("[CacheEntry] getId");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getIdObject() {
		System.out.println("[CacheEntry] getIdObject");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPriority() {
		System.out.println("[CacheEntry] getPriority");
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSharingPolicy() {
		System.out.println("[CacheEntry] getSharingPolicy");
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Enumeration getTemplates() {
		System.out.println("[CacheEntry] getTemplates");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTimeLimit() {
		System.out.println("[CacheEntry] getTimeLimit");
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getTimeStamp() {
		System.out.println("[CacheEntry] getTimeStamp");
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getUserMetaData() {
		System.out.println("[CacheEntry] getUserMetaData");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getValidatorExpirationTime() {
		System.out.println("[CacheEntry] getValidatorExpirationTime");
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getValue() {
		System.out.println("[CacheEntry] getValue");
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public boolean isBatchEnabled() {
		System.out.println("[CacheEntry] isBatchEnabled");
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInvalid() {
		System.out.println("[CacheEntry] isInvalid");
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean prepareForSerialization() {
		System.out.println("[CacheEntry] prepareForSerialization");
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refreshEntry() {
		System.out.println("[CacheEntry] refreshEntry");
		// TODO Auto-generated method stub
		
	}

}
