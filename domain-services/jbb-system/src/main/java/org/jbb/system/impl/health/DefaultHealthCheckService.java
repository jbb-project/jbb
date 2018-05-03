/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.health;

import com.codahale.metrics.health.HealthCheck.Result;
import com.codahale.metrics.health.HealthCheckRegistry;
import java.time.LocalDateTime;
import java.util.SortedMap;
import lombok.RequiredArgsConstructor;
import org.jbb.system.api.health.HealthCheckService;
import org.jbb.system.api.health.HealthResult;
import org.jbb.system.api.health.HealthStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultHealthCheckService implements HealthCheckService {

    private final HealthCheckRegistry healthCheckRegistry;

    @Override
    public HealthResult getHealth() {
        return HealthResult.builder()
            .status(resolveStatus())
            .lastCheckedAt(LocalDateTime.now())
            .build();
    }

    private HealthStatus resolveStatus() {
        SortedMap<String, Result> results = healthCheckRegistry.runHealthChecks();
        boolean healthy = results.values().stream().allMatch(Result::isHealthy);
        return HealthStatus.getStatus(healthy);
    }
}
