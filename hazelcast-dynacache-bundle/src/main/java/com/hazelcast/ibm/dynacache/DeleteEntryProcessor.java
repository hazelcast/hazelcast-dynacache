package com.hazelcast.ibm.dynacache;

import com.hazelcast.map.AbstractEntryProcessor;

import java.util.Map;

class DeleteEntryProcessor extends AbstractEntryProcessor {
    @Override
    public Object process(Map.Entry entry) {
        //noinspection unchecked
        entry.setValue(null);

        return Boolean.TRUE;
    }
}
