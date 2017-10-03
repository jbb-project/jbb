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

import static org.mockito.BDDMockito.given;

import org.jbb.lib.cache.CacheProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

@RunWith(MockitoJUnitRunner.class)
public class CacheProvidersServiceTest {

    @Mock
    private CacheProperties cachePropertiesMock;

    @Mock
    private ApplicationContext applicationContextMock;

    @InjectMocks
    private CacheProvidersService cacheProvidersService;

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateException_whenUnknownProviderName() throws Exception {
        // given
        String unknownProviderName = "terracotta";
        given(cachePropertiesMock.providerName()).willReturn(unknownProviderName);

        // when
        cacheProvidersService.getCurrentCacheProvider();

        // then
        // throw IllegalStateException
    }
}