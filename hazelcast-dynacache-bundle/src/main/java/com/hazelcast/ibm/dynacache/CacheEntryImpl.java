package com.hazelcast.ibm.dynacache;

import com.ibm.websphere.cache.CacheEntry;
import com.ibm.websphere.cache.EntryInfo;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.logging.Logger;

public class CacheEntryImpl implements CacheEntry, Serializable {

    private static Logger LOGGER = Logger.getLogger(CacheEntryImpl.class.getName());

    private Object value;
	private EntryInfo entryInfo;

	public CacheEntryImpl(Object value, EntryInfo ei) {
		this.value = value;
		this.entryInfo = ei;
	}

	@Override
	public void finish() {
		LOGGER.finest("[CacheEntry] Finish");
	}

	@Override
	public Enumeration getAliasList() {
		LOGGER.finest("[CacheEntry] getAliasList");

		return entryInfo.getAliasList();
	}

	@Override
	public int getCacheType() {
		LOGGER.finest("[CacheEntry] getCacheType");

		return entryInfo.getCacheType();
	}

	@Override
	public long getCacheValueSize() {
		LOGGER.finest("[CacheEntry] getCacheValueSize");

        return 0;
	}

	@Override
	public long getCreationTime() {
		LOGGER.finest("[CacheEntry] getCreationTime");

        return 0;
	}

	@Override
	public Enumeration getDataIds() {
		LOGGER.finest("[CacheEntry] getDataIds");

        return entryInfo.getDataIds();
	}

	@Override
	public byte[] getDisplayValue() {
		LOGGER.finest("[CacheEntry] getDisplayValue");

        return null;
	}

	@Override
	public long getExpirationTime() {
		LOGGER.finest("[CacheEntry] getExpirationTime");

        return entryInfo.getExpirationTime();
	}

	@Override
	public String getExternalCacheGroupId() {
		LOGGER.finest("[CacheEntry] getExternalCacheGroupId");

        return null;
	}

	@Override
	public String getId() {
		LOGGER.finest("[CacheEntry] getId");

		return entryInfo.getId();
	}

	@Override
	public Object getIdObject() {
		LOGGER.finest("[CacheEntry] getIdObject");

		return entryInfo.getIdObject();
	}

	@Override
	public int getPriority() {
		LOGGER.finest("[CacheEntry] getPriority");

        return entryInfo.getPriority();
	}

	@Override
	public int getSharingPolicy() {
		LOGGER.finest("[CacheEntry] getSharingPolicy");

        return entryInfo.getSharingPolicy();
	}

	@Override
	public Enumeration getTemplates() {
		LOGGER.finest("[CacheEntry] getTemplates");

        return entryInfo.getTemplates();
	}

	@Override
	public int getTimeLimit() {
		LOGGER.finest("[CacheEntry] getTimeLimit");

        return entryInfo.getTimeLimit();
	}

	@Override
	public long getTimeStamp() {
		LOGGER.finest("[CacheEntry] getTimeStamp");

        return 0;
	}

	@Override
	public Object getUserMetaData() {
		LOGGER.finest("[CacheEntry] getUserMetaData");

        return entryInfo.getUserMetaData();
	}

	@Override
	public long getValidatorExpirationTime() {
		LOGGER.finest("[CacheEntry] getValidatorExpirationTime");

        return entryInfo.getValidatorExpirationTime();
	}

	@Override
	public Object getValue() {
		LOGGER.finest("[CacheEntry] getValue");

        return value;
	}

	@Override
	public boolean isBatchEnabled() {
		LOGGER.finest("[CacheEntry] isBatchEnabled");

		//noinspection deprecation
		return entryInfo.isBatchEnabled();
	}

	@Override
	public boolean isInvalid() {
		LOGGER.finest("[CacheEntry] isInvalid");

        return false;
	}

	@Override
	public boolean prepareForSerialization() {
		LOGGER.finest("[CacheEntry] prepareForSerialization");

        return false;
	}

	@Override
	public void refreshEntry() {
		LOGGER.finest("[CacheEntry] refreshEntry");
	}
}
