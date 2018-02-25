/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.cache.hazelcast;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;

import org.jbb.lib.cache.CacheConfig;
import org.jbb.lib.cache.TestbedCacheConfig;
import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommonsConfig.class, MockCommonsConfig.class, CacheConfig.class,
        TestbedCacheConfig.class, PropertiesConfig.class})
public class HazelcastConfigFilesManagerIT {

    @Autowired
    private HazelcastConfigFilesManager hazelcastConfigFilesManager;

    @Test
    public void shouldUpdateHazelcastClientConfig() throws Exception {
        ClientConfig hazelcastClientConfig = hazelcastConfigFilesManager.getHazelcastClientConfig();
        hazelcastClientConfig.getGroupConfig().setName("hazelcast-testbed");
        hazelcastConfigFilesManager.setHazelcastClientConfig(hazelcastClientConfig);

        ClientConfig newHazelcastClientConfig = hazelcastConfigFilesManager
                .getHazelcastClientConfig();
        assertThat(newHazelcastClientConfig.getGroupConfig().getName())
                .isEqualTo("hazelcast-testbed");
    }

    @Test
    public void shouldUpdateHazelcastServerConfig() throws Exception {
        Config hazelcastServerConfig = hazelcastConfigFilesManager.getHazelcastServerConfig();
        hazelcastServerConfig.getGroupConfig().setName("hazelcast-test-serv");
        hazelcastConfigFilesManager.setHazelcastServerConfig(hazelcastServerConfig);

        Config newHazelcastServerConfig = hazelcastConfigFilesManager.getHazelcastServerConfig();
        assertThat(newHazelcastServerConfig.getGroupConfig().getName())
                .isEqualTo("hazelcast-test-serv");
    }
}