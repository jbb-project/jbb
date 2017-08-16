/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.cache;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JbbCacheManagerTest {

    @Mock
    private ProxyJCacheManager proxyJCacheManagerMock;
    @Mock
    private ProxySpringCacheManager proxySpringCacheManagerMock;
    @Mock
    private SpringCacheManagerFactory springCacheManagerFactoryMock;
    @Mock
    private ManagedHazelcastInstance managedHazelcastInstanceMock;

    @InjectMocks
    private JbbCacheManager jbbCacheManager;

    @Test
    public void closeProxyIfApplicable_whenRefresh() throws Exception {
        // given
        given(proxyJCacheManagerMock.isClosed()).willReturn(false);

        // when
        jbbCacheManager.refresh();

        // then
        verify(proxyJCacheManagerMock).close();
    }

    @Test
    public void tryToShutdownHazelcastInstance_whenRefresh() throws Exception {
        // when
        jbbCacheManager.refresh();

        // then
        verify(managedHazelcastInstanceMock).shutdownIfApplicable();
    }
}