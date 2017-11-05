/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.cache.logic.install;

import org.jbb.install.cache.CacheInstallationData;
import org.jbb.install.cache.HazelcastServerInstallationData;
import org.jbb.system.api.cache.CacheProvider;
import org.jbb.system.api.cache.CacheSettings;
import org.jbb.system.api.cache.HazelcastServerSettings;
import org.springframework.stereotype.Component;

@Component
public class HazelcastServerInstaller implements CacheProviderInstaller {

    @Override
    public boolean isApplicable(CacheProvider cacheProvider) {
        return cacheProvider == CacheProvider.HAZELCAST_SERVER;
    }

    @Override
    public void apply(CacheInstallationData cacheInstallationData, CacheSettings cacheSettings) {
        HazelcastServerSettings hazelcastServerSettings = cacheSettings
            .getHazelcastServerSettings();
        HazelcastServerInstallationData serverInstallationData = cacheInstallationData
            .getHazelcastServerInstallationData();
        hazelcastServerSettings.setMembers(serverInstallationData.getMembers());
        hazelcastServerSettings.setGroupName(serverInstallationData.getGroupName());
        hazelcastServerSettings.setGroupPassword(serverInstallationData.getGroupPassword());
        hazelcastServerSettings.setServerPort(serverInstallationData.getServerPort());
        hazelcastServerSettings
            .setManagementCenterEnabled(serverInstallationData.getManagementCenterEnabled());
        hazelcastServerSettings
            .setManagementCenterUrl(serverInstallationData.getManagementCenterUrl());
    }
}
