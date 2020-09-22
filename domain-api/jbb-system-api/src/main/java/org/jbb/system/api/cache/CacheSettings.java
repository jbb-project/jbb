/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.cache;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheSettings {

    private boolean applicationCacheEnabled;

    private boolean secondLevelCacheEnabled;

    private boolean queryCacheEnabled;

    @NotNull
    @Valid
    private HazelcastServerSettings hazelcastServerSettings;

    @NotNull
    @Valid
    private HazelcastClientSettings hazelcastClientSettings;

    @NotNull
    private CacheProvider currentCacheProvider;
}
