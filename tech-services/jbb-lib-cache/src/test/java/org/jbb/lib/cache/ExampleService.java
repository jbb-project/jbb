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

import org.mockito.Mockito;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;

import lombok.Getter;

public class ExampleService {
    @Getter
    private ExternalResource externalResource = Mockito.mock(ExternalResource.class);

    private Integer counter = 0;

    @CacheResult(cacheName = "testbed-cache")
    public Integer sendRequestToExternalResource() {
        externalResource.sendRequest();
        return counter++;
    }

    @CacheRemoveAll(cacheName = "testbed-cache")
    public void invalidCache() {

    }

    public interface ExternalResource {
        void sendRequest();
    }

}
