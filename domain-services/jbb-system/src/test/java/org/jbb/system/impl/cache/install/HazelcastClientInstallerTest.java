/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.cache.install;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Lists;
import org.jbb.install.cache.CacheInstallationData;
import org.jbb.install.cache.HazelcastClientInstallationData;
import org.jbb.system.api.cache.CacheProvider;
import org.jbb.system.api.cache.CacheSettings;
import org.jbb.system.api.cache.HazelcastClientSettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HazelcastClientInstallerTest {

    @InjectMocks
    private HazelcastClientInstaller hazelcastClientInstaller;

    @Test
    public void hazelcastClientShouldBeApplicable() throws Exception {
        // when
        boolean applicable = hazelcastClientInstaller.isApplicable(CacheProvider.HAZELCAST_CLIENT);

        // then
        assertThat(applicable).isTrue();
    }

    @Test
    public void installationDataShouldBeAppliedToCacheSettings() throws Exception {
        // given
        CacheInstallationData cacheInstallationData = CacheInstallationData.builder()
            .hazelcastClientInstallationData(HazelcastClientInstallationData.builder()
                .groupName("hazelcast-group")
                .groupPassword("hz-pass")
                .members(Lists.newArrayList("127.0.0.1:5700", "127.0.0.1:5701"))
                .build())
            .build();

        CacheSettings cacheSettings = CacheSettings.builder()
            .hazelcastClientSettings(new HazelcastClientSettings())
            .build();

        // when
        hazelcastClientInstaller.apply(cacheInstallationData, cacheSettings);

        // then
        HazelcastClientSettings hazelcastClientSettings = cacheSettings
            .getHazelcastClientSettings();
        assertThat(hazelcastClientSettings.getGroupName()).isEqualTo("hazelcast-group");
        assertThat(hazelcastClientSettings.getGroupPassword()).isEqualTo("hz-pass");
        assertThat(hazelcastClientSettings.getMembers())
            .isEqualTo(Lists.newArrayList("127.0.0.1:5700", "127.0.0.1:5701"));
    }
}