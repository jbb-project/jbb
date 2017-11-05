/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.install.logic.auto;

import static org.jbb.system.api.cache.CacheUtils.buildHazelcastMemberList;

import java.util.Optional;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.lang3.EnumUtils;
import org.jbb.install.InstallationData;
import org.jbb.install.cache.CacheInstallationData;
import org.jbb.install.cache.CacheType;
import org.jbb.install.cache.HazelcastClientInstallationData;
import org.jbb.install.cache.HazelcastServerInstallationData;
import org.springframework.stereotype.Component;

@Component
public class CacheAutoInstallDataReader implements AutoInstallationDataReader {

    private static final String CACHE_TYPE = "cache.type";

    private static final String HAZELCAST_SERVER_MEMBERS = "cache.hazelcast.server.members";
    private static final String HAZELCAST_SERVER_GROUP_NAME = "cache.hazelcast.server.group.name";
    private static final String HAZELCAST_SERVER_GROUP_PSWD = "cache.hazelcast.server.group.password";
    private static final String HAZELCAST_SERVER_PORT = "cache.hazelcast.server.port";
    private static final String HAZELCAST_SERVER_MANAGEMENT_CENTER_ENABLED = "cache.hazelcast.server.mancenter.enabled";
    private static final String HAZELCAST_SERVER_MANAGEMENT_CENTER_URL = "cache.hazelcast.server.mancenter.url";

    private static final String HAZELCAST_CLIENT_MEMBERS = "cache.hazelcast.client.members";
    private static final String HAZELCAST_CLIENT_GROUP_NAME = "cache.hazelcast.client.group.name";
    private static final String HAZELCAST_CLIENT_GROUP_PSWD = "cache.hazelcast.client.group.password";

    @Override
    public InstallationData updateInstallationData(InstallationData data,
        FileBasedConfiguration configuration) {
        data.setCacheInstallationData(Optional.of(buildCacheData(configuration)));
        return data;
    }

    private CacheInstallationData buildCacheData(FileBasedConfiguration configuration) {
        CacheType cacheType = EnumUtils
            .getEnum(CacheType.class, configuration.getString(CACHE_TYPE, null));
        if (cacheType == null) {
            cacheType = CacheType.CAFFEINE;
        }
        return CacheInstallationData.builder()
            .cacheType(cacheType)
            .hazelcastServerInstallationData(HazelcastServerInstallationData.builder()
                .members(buildHazelcastMemberList(
                    configuration.getString(HAZELCAST_SERVER_MEMBERS, null), false))
                .groupName(configuration.getString(HAZELCAST_SERVER_GROUP_NAME, null))
                .groupPassword(configuration.getString(HAZELCAST_SERVER_GROUP_PSWD, null))
                .serverPort(configuration.getInt(HAZELCAST_SERVER_PORT, 0))
                .managementCenterEnabled(
                    configuration.getBoolean(HAZELCAST_SERVER_MANAGEMENT_CENTER_ENABLED))
                .managementCenterUrl(
                    configuration.getString(HAZELCAST_SERVER_MANAGEMENT_CENTER_URL, null))
                .build())
            .hazelcastClientInstallationData(HazelcastClientInstallationData.builder()
                .members(buildHazelcastMemberList(
                    configuration.getString(HAZELCAST_CLIENT_MEMBERS, null), true))
                .groupName(configuration.getString(HAZELCAST_CLIENT_GROUP_NAME, null))
                .groupPassword(configuration.getString(HAZELCAST_CLIENT_GROUP_PSWD, null))
                .build())
            .build();
    }
}
