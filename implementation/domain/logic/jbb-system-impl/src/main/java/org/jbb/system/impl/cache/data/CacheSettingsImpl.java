/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.cache.data;

import org.jbb.system.api.model.cache.CacheSettings;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CacheSettingsImpl implements CacheSettings {

    private boolean applicationCacheEnabled;

    private boolean secondLevelCacheEnabled;

    private boolean queryCacheEnabled;

}
