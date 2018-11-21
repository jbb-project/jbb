/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.cache;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CachingIT extends BaseIT {

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
