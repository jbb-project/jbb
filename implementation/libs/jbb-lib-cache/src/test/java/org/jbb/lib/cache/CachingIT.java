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

import org.jbb.lib.core.CoreConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCoreConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class, MockCoreConfig.class, CacheConfig.class, TestbedCacheConfig.class, PropertiesConfig.class})
public class CachingIT {
    @Autowired
    private ExampleService exampleService;

    @Test
    public void resultShouldBeCachable_andCacheCanBeInvalidated() throws Exception {
        Integer firstResult = exampleService.sendRequestToExternalResource();
        assertThat(firstResult).isEqualTo(0);
        verify(exampleService.getExternalResource(), times(1)).sendRequest();

        Integer secondResult = exampleService.sendRequestToExternalResource();
        assertThat(secondResult).isEqualTo(firstResult);
        verify(exampleService.getExternalResource(), times(1)).sendRequest();

        exampleService.invalidCache();
        Integer thirdResult = exampleService.sendRequestToExternalResource();
        assertThat(thirdResult).isEqualTo(1);
        verify(exampleService.getExternalResource(), times(2)).sendRequest();
    }
}
