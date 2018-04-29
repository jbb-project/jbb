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
import org.jbb.install.cache.HazelcastServerInstallationData;
import org.jbb.system.api.cache.CacheProvider;
import org.jbb.system.api.cache.CacheSettings;
import org.jbb.system.api.cache.HazelcastServerSettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HazelcastServerInstallerTest {

    @InjectMocks
    private HazelcastServerInstaller hazelcastServerInstaller;

    @Test
    public void hazelcastServerShouldBeApplicable() throws Exception {
        // when
        boolean applicable = hazelcastServerInstaller.isApplicable(CacheProvider.HAZELCAST_SERVER);

        // then
        assertThat(applicable).isTrue();
    }

    @Test
    public void installationDataShouldBeAppliedToCacheSettings() throws Exception {
        // given
        CacheInstallationData cacheInstallationData = CacheInstallationData.builder()
            .hazelcastServerInstallationData(HazelcastServerInstallationData.builder()
                .groupName("hazelcast")
                .groupPassword("hzpass")
                .members(Lists.newArrayList("127.0.0.1:5700", "127.0.0.1:5701"))
                .serverPort(5666)
                .managementCenterUrl("127.0.0.1:8080")
                .managementCenterEnabled(true)
                .build())
            .build();

        CacheSettings cacheSettings = CacheSettings.builder()
            .hazelcastServerSettings(new HazelcastServerSettings())
            .build();

        // when
        hazelcastServerInstaller.apply(cacheInstallationData, cacheSettings);

        // then
        HazelcastServerSettings hazelcastServerSettings = cacheSettings
            .getHazelcastServerSettings();
        assertThat(hazelcastServerSettings.getGroupName()).isEqualTo("hazelcast");
        assertThat(hazelcastServerSettings.getGroupPassword()).isEqualTo("hzpass");
        assertThat(hazelcastServerSettings.getMembers())
            .isEqualTo(Lists.newArrayList("127.0.0.1:5700", "127.0.0.1:5701"));
        assertThat(hazelcastServerSettings.getServerPort()).isEqualTo(5666);
        assertThat(hazelcastServerSettings.getManagementCenterUrl()).isEqualTo("127.0.0.1:8080");
        assertThat(hazelcastServerSettings.isManagementCenterEnabled()).isTrue();
    }

}