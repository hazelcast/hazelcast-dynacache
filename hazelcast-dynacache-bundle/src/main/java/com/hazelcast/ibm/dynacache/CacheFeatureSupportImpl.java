/*
* Copyright 2020 Hazelcast Inc.
*
* Licensed under the Hazelcast Community License (the "License"); you may not use
* this file except in compliance with the License. You may obtain a copy of the
* License at
*
* http://hazelcast.com/hazelcast-community-license
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OF ANY KIND, either express or implied. See the License for the
* specific language governing permissions and limitations under the License.
*/

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
