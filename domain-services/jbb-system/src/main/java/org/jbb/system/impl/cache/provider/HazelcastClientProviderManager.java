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

import com.hazelcast.client.config.ClientConfig;

import org.jbb.lib.cache.JbbCacheManager;
import org.jbb.lib.cache.hazelcast.HazelcastConfigFilesManager;
import org.jbb.system.api.cache.CacheProvider;
import org.jbb.system.api.cache.CacheSettings;
import org.jbb.system.api.cache.HazelcastClientSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HazelcastClientProviderManager implements
        CacheProviderManager<HazelcastClientSettings> {

    public static final String PROVIDER_PROPERTY_VALUE = JbbCacheManager.HAZELCAST_CLIENT_PROVIDER_NAME;

    private final HazelcastConfigFilesManager hazelcastConfigFilesManager;

    @Override
    public CacheProvider getProviderName() {
        return CacheProvider.HAZELCAST_CLIENT;
    }

    @Override
    public HazelcastClientSettings getCurrentProviderSettings() {
        ClientConfig hazelcastClientConfig = hazelcastConfigFilesManager.getHazelcastClientConfig();
        HazelcastClientSettings settings = new HazelcastClientSettings();
        settings.setGroupName(hazelcastClientConfig.getGroupConfig().getName());
        settings.setGroupPassword(hazelcastClientConfig.getGroupConfig().getPassword());
        settings.setMembers(hazelcastClientConfig.getNetworkConfig().getAddresses());
        settings.setConnectionAttemptLimit(
                hazelcastClientConfig.getNetworkConfig().getConnectionAttemptLimit());
        settings.setConnectionAttemptPeriod(Duration
                .ofMillis(hazelcastClientConfig.getNetworkConfig().getConnectionAttemptPeriod()));
        settings.setConnectionTimeout(
                Duration.ofMillis(hazelcastClientConfig.getNetworkConfig().getConnectionTimeout()));
        return settings;
    }

    @Override
    public void setProviderSettings(CacheSettings newCacheSettings) {
        HazelcastClientSettings newHazelcastClientSettings = newCacheSettings
                .getHazelcastClientSettings();

        ClientConfig clientConfig = hazelcastConfigFilesManager.getHazelcastClientConfig();
        clientConfig.getGroupConfig().setName(newHazelcastClientSettings.getGroupName());
        clientConfig.getGroupConfig().setPassword(newHazelcastClientSettings.getGroupPassword());
        clientConfig.getNetworkConfig().setAddresses(newHazelcastClientSettings.getMembers());
        clientConfig.getNetworkConfig()
                .setConnectionAttemptLimit(newHazelcastClientSettings.getConnectionAttemptLimit());
        clientConfig.getNetworkConfig().setConnectionAttemptPeriod(
                Math.toIntExact(newHazelcastClientSettings.getConnectionAttemptPeriod().toMillis()));
        clientConfig.getNetworkConfig().setConnectionTimeout(
                Math.toIntExact(newHazelcastClientSettings.getConnectionTimeout().toMillis()));

        hazelcastConfigFilesManager.setHazelcastClientConfig(clientConfig);
    }
}
