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

import com.github.zafarkhaja.semver.Version;

import org.apache.commons.lang3.EnumUtils;
import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.install.JbbVersions;
import org.jbb.install.cache.CacheInstallationData;
import org.jbb.system.api.cache.CacheProvider;
import org.jbb.system.api.cache.CacheSettings;
import org.jbb.system.api.cache.CacheSettingsService;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@DependsOn("cachePropertiesPropertyListener")
public class CacheInstallAction implements InstallUpdateAction {

    private final List<CacheProviderInstaller> installers;

    private final CacheSettingsService cacheSettingsService;

    @Override
    public Version fromVersion() {
        return JbbVersions.VERSION_0_9_0;
    }

    @Override
    public void install(InstallationData installationData) {
        Optional<CacheInstallationData> cacheDataOptional = installationData
                .getCacheInstallationData();
        if (!cacheDataOptional.isPresent()) {
            return;
        }

        CacheInstallationData cacheData = cacheDataOptional.get();
        CacheSettings cacheSettings = cacheSettingsService.getCacheSettings();
        CacheProvider cacheProvider = EnumUtils
                .getEnum(CacheProvider.class, cacheData.getCacheType().toString());
        cacheSettings.setCurrentCacheProvider(cacheProvider);

        for (CacheProviderInstaller installer : installers) {
            if (installer.isApplicable(cacheProvider)) {
                installer.apply(cacheData, cacheSettings);
                break;
            }
        }

        cacheSettingsService.setCacheSettings(cacheSettings);
    }
}
