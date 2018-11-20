/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.cache.hazelcast.health;

import org.jbb.lib.cache.CacheProperties;
import org.jbb.lib.cache.ManagedHazelcastInstance;
import org.jbb.lib.health.JbbHealthCheck;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import static org.jbb.lib.cache.JbbCacheManager.CAFFEINE_PROVIDER_NAME;

@Component
@RequiredArgsConstructor
public class HazelcastHealthCheck extends JbbHealthCheck {

    private final CacheProperties cacheProperties;
    private final ManagedHazelcastInstance managedHazelcastInstance;

    @Override
    public String getName() {
        return "Hazelcast connection";
    }

    @Override
    protected Result check() {
        if (CAFFEINE_PROVIDER_NAME.equals(cacheProperties.providerName())) {
            return Result.healthy("Caffeine is being used as cache provider");
        }

        if (!managedHazelcastInstance.getLifecycleService().isRunning()) {
            return Result.unhealthy("Hazelcast instance is not running");
        }

        if (managedHazelcastInstance.getCluster().getMembers().isEmpty()) {
            return Result.unhealthy("Hazelcast cluster is empty");
        }

        if (!managedHazelcastInstance.getPartitionService().isClusterSafe()) {
            return Result.unhealthy("Hazelcast cluster is not in safe state");
        }

        return Result.healthy();
    }
}
