/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.cache.hazelcast;

import static org.assertj.core.api.Assertions.assertThat;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import org.jbb.lib.cache.BaseIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HazelcastConfigFilesManagerIT extends BaseIT {

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