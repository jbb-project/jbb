/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.cache.hazelcast.health;

import com.google.common.collect.Sets;

import com.codahale.metrics.health.HealthCheck;
import com.hazelcast.core.Cluster;
import com.hazelcast.core.LifecycleService;
import com.hazelcast.core.Member;
import com.hazelcast.core.PartitionService;

import org.jbb.lib.cache.CacheProperties;
import org.jbb.lib.cache.ManagedHazelcastInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jbb.lib.cache.JbbCacheManager.CAFFEINE_PROVIDER_NAME;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class HazelcastHealthCheckTest {

    @Mock
    private CacheProperties cachePropertiesMock;

    @Mock
    private ManagedHazelcastInstance managedHazelcastInstanceMock;

    @InjectMocks
    private HazelcastHealthCheck hazelcastHealthCheck;

    @Test
    public void shouldReturnNotEmptyName() {
        // when
        String checkName = hazelcastHealthCheck.getName();

        // then
        assertThat(checkName).isNotBlank();
    }

    @Test
    public void shouldBeHealthy_whenCaffeineIsBeenUsed() throws Exception {
        // given
        given(cachePropertiesMock.providerName()).willReturn(CAFFEINE_PROVIDER_NAME);

        // when
        HealthCheck.Result result = hazelcastHealthCheck.check();

        // then
        assertThat(result.isHealthy()).isTrue();
    }

    @Test
    public void shouldBeUnhealthy_whenHazelcastLifecycleServiceIsNotRunning() throws Exception {
        // given
        LifecycleService lifecycleServiceMock = mock(LifecycleService.class);
        given(managedHazelcastInstanceMock.getLifecycleService()).willReturn(lifecycleServiceMock);
        given(lifecycleServiceMock.isRunning()).willReturn(false);

        // when
        HealthCheck.Result result = hazelcastHealthCheck.check();

        // then
        assertThat(result.isHealthy()).isFalse();
    }

    @Test
    public void shouldBeUnhealthy_whenHazelcastClusterIsEmpty() throws Exception {
        // given
        LifecycleService lifecycleServiceMock = mock(LifecycleService.class);
        Cluster clusterMock = mock(Cluster.class);

        given(managedHazelcastInstanceMock.getLifecycleService()).willReturn(lifecycleServiceMock);
        given(managedHazelcastInstanceMock.getCluster()).willReturn(clusterMock);

        given(lifecycleServiceMock.isRunning()).willReturn(true);
        given(clusterMock.getMembers()).willReturn(Sets.newHashSet());

        // when
        HealthCheck.Result result = hazelcastHealthCheck.check();

        // then
        assertThat(result.isHealthy()).isFalse();
    }

    @Test
    public void shouldBeUnhealthy_whenHazelcastClusterIsNotSafe() throws Exception {
        // given
        LifecycleService lifecycleServiceMock = mock(LifecycleService.class);
        Cluster clusterMock = mock(Cluster.class);
        PartitionService partitionServiceMock = mock(PartitionService.class);

        given(managedHazelcastInstanceMock.getLifecycleService()).willReturn(lifecycleServiceMock);
        given(managedHazelcastInstanceMock.getCluster()).willReturn(clusterMock);
        given(managedHazelcastInstanceMock.getPartitionService()).willReturn(partitionServiceMock);

        given(lifecycleServiceMock.isRunning()).willReturn(true);
        given(clusterMock.getMembers()).willReturn(Sets.newHashSet(mock(Member.class)));
        given(partitionServiceMock.isClusterSafe()).willReturn(false);

        // when
        HealthCheck.Result result = hazelcastHealthCheck.check();

        // then
        assertThat(result.isHealthy()).isFalse();
    }

    @Test
    public void shouldBeHealthy_whenHazelcastClusterIsOk() throws Exception {
        // given
        LifecycleService lifecycleServiceMock = mock(LifecycleService.class);
        Cluster clusterMock = mock(Cluster.class);
        PartitionService partitionServiceMock = mock(PartitionService.class);

        given(managedHazelcastInstanceMock.getLifecycleService()).willReturn(lifecycleServiceMock);
        given(managedHazelcastInstanceMock.getCluster()).willReturn(clusterMock);
        given(managedHazelcastInstanceMock.getPartitionService()).willReturn(partitionServiceMock);

        given(lifecycleServiceMock.isRunning()).willReturn(true);
        given(clusterMock.getMembers()).willReturn(Sets.newHashSet(mock(Member.class)));
        given(partitionServiceMock.isClusterSafe()).willReturn(true);

        // when
        HealthCheck.Result result = hazelcastHealthCheck.check();

        // then
        assertThat(result.isHealthy()).isTrue();
    }
}