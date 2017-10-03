/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.cache.logic.provider;

import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.lib.cache.hazelcast.HazelcastConfigFilesManager;
import org.jbb.system.api.cache.CacheProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HazelcastServerProviderManagerTest {

    @InjectMocks
    HazelcastServerProviderManager hazelcastServerProviderManager;
    @Mock
    private HazelcastConfigFilesManager hazelcastConfigFilesManagerMock;

    @Test
    public void shouldReturnCorrectProvider() throws Exception {
        // when
        CacheProvider providerName = hazelcastServerProviderManager.getProviderName();

        // then
        assertThat(providerName).isEqualTo(CacheProvider.HAZELCAST_SERVER);
    }

}