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

import org.jbb.system.api.cache.CacheProvider;
import org.jbb.system.api.cache.CacheProviderSettings;
import org.jbb.system.api.cache.CacheSettings;

public interface CacheProviderManager<T extends CacheProviderSettings> {

    CacheProvider getProviderName();

    T getCurrentProviderSettings();

    void setProviderSettings(CacheSettings newCacheSettings);

    default void setAsCurrentProvider(CacheSettings newCacheSettings) {
        newCacheSettings.setCurrentCacheProvider(getProviderName());
    }

}
