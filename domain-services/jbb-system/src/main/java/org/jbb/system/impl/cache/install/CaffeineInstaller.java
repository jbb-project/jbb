/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.cache.install;

import org.jbb.install.cache.CacheInstallationData;
import org.jbb.system.api.cache.CacheProvider;
import org.jbb.system.api.cache.CacheSettings;
import org.springframework.stereotype.Component;

@Component
public class CaffeineInstaller implements CacheProviderInstaller {

    @Override
    public boolean isApplicable(CacheProvider cacheProvider) {
        return cacheProvider == CacheProvider.CAFFEINE;
    }

    @Override
    public void apply(CacheInstallationData cacheInstallationData, CacheSettings cacheSettings) {
        // nothing to do...
    }
}
