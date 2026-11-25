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

package com.hazelcast.client.osgi;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.DistributedObjectEvent;
import com.hazelcast.core.DistributedObjectListener;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.ibm.dynacache.CoreCacheImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class HazelcastOSGiClientInstanceTest {

    private static HazelcastInstance member;
    private static HazelcastInstance client;
    private static HazelcastOSGiClientInstance wrapper;

    @BeforeClass
    public static void connectClient() {
        Config memberConfig = new Config().setClusterName(UUID.randomUUID().toString());
        memberConfig.getNetworkConfig().setPort(0).getInterfaces().setEnabled(true).addInterface("127.0.0.1");
        memberConfig.getNetworkConfig().getJoin().getAutoDetectionConfig().setEnabled(false);
        memberConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        memberConfig.setProperty("hazelcast.phone.home.enabled", "false");
        member = Hazelcast.newHazelcastInstance(memberConfig);

        ClientConfig clientConfig = new ClientConfig().setClusterName(memberConfig.getClusterName());
        clientConfig.getNetworkConfig().addAddress("127.0.0.1:" + member.getCluster().getLocalMember().getAddress().getPort());
        clientConfig.getConnectionStrategyConfig().getConnectionRetryConfig().setClusterConnectTimeoutMillis(5_000);
        client = HazelcastClient.newHazelcastClient(clientConfig);
        wrapper = new HazelcastOSGiClientInstanceImpl(client, null);
    }

    @AfterClass
    public static void shutdown() {
        try {
            if (client != null) {
                client.shutdown();
            }
        } finally {
            if (member != null) {
                member.shutdown();
            }
        }
    }

    @Test
    public void removesDistributedObjectListenerByUuid() {
        UUID registrationId = wrapper.addDistributedObjectListener(new DistributedObjectListener() {
            @Override
            public void distributedObjectCreated(DistributedObjectEvent event) {
            }

            @Override
            public void distributedObjectDestroyed(DistributedObjectEvent event) {
            }
        });

        assertTrue(wrapper.removeDistributedObjectListener(registrationId));
    }

    @Test
    public void invalidatesCacheEntriesByDependencyThroughClient() {
        CoreCacheImpl cache = new CoreCacheImpl("test-cache", wrapper);
        member.getMap("test-cache").put("entry", "cached value");
        member.getMultiMap("test-cache-dep").put("dependency", "entry");
        assertTrue(cache.containsCacheId("entry"));

        cache.invalidateByDependency("dependency", true);

        assertFalse(member.getMap("test-cache").containsKey("entry"));
    }

    @Test
    public void delegatesSqlService() {
        assertSame(client.getSql(), wrapper.getSql());
    }

    @Test
    public void reportsDataConnectionServiceUnsupportedOnTheClient() {
        assertThrows(UnsupportedOperationException.class, wrapper::getDataConnectionService);
    }
}
