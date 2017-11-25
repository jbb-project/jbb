/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.cache.provider;

import com.hazelcast.config.Config;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.cache.JbbCacheManager;
import org.jbb.lib.cache.hazelcast.HazelcastConfigFilesManager;
import org.jbb.system.api.cache.CacheProvider;
import org.jbb.system.api.cache.CacheSettings;
import org.jbb.system.api.cache.HazelcastServerSettings;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HazelcastServerProviderManager implements
    CacheProviderManager<HazelcastServerSettings> {

    public static final String PROVIDER_PROPERTY_VALUE = JbbCacheManager.HAZELCAST_SERVER_PROVIDER_NAME;

    private final HazelcastConfigFilesManager hazelcastConfigFilesManager;

    @Override
    public CacheProvider getProviderName() {
        return CacheProvider.HAZELCAST_SERVER;
    }

    @Override
    public HazelcastServerSettings getCurrentProviderSettings() {
        Config hazelcastServerConfig = hazelcastConfigFilesManager.getHazelcastServerConfig();

        HazelcastServerSettings settings = new HazelcastServerSettings();
        settings.setGroupName(hazelcastServerConfig.getGroupConfig().getName());
        settings.setGroupPassword(hazelcastServerConfig.getGroupConfig().getPassword());
        settings.setMembers(
            hazelcastServerConfig.getNetworkConfig().getJoin().getTcpIpConfig().getMembers());
        settings.setServerPort(hazelcastServerConfig.getNetworkConfig().getPort());
        settings.setManagementCenterEnabled(
            hazelcastServerConfig.getManagementCenterConfig().isEnabled());
        settings.setManagementCenterUrl(hazelcastServerConfig.getManagementCenterConfig().getUrl());

        return settings;
    }

    @Override
    public void setProviderSettings(CacheSettings newCacheSettings) {
        HazelcastServerSettings newHazelcastServerSettings = newCacheSettings
            .getHazelcastServerSettings();

        Config config = hazelcastConfigFilesManager.getHazelcastServerConfig();
        config.getGroupConfig().setName(newHazelcastServerSettings.getGroupName());
        config.getGroupConfig().setPassword(newHazelcastServerSettings.getGroupPassword());
        config.getNetworkConfig().getJoin().getTcpIpConfig()
            .setMembers(newHazelcastServerSettings.getMembers());
        config.getNetworkConfig().setPort(newHazelcastServerSettings.getServerPort());
        config.getManagementCenterConfig()
            .setEnabled(newHazelcastServerSettings.isManagementCenterEnabled());
        config.getManagementCenterConfig()
            .setUrl(newHazelcastServerSettings.getManagementCenterUrl());

        hazelcastConfigFilesManager.setHazelcastServerConfig(config);

    }
}
