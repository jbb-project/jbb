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
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.system.api.health.HealthResult;
import org.jbb.system.api.health.HealthStatus;
import org.jbb.system.event.ApplicationGetsUnhealthyEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HealthCheckManager {

    private final HealthCheckRegistry healthCheckRegistry;
    private final JbbEventBus eventBus;

    private SortedMap<String, Result> healthResults;
    private LocalDateTime lastRunDateTime;

    @PostConstruct
    @Scheduled(fixedDelay = 30_000)
    public void runHealthChecks() {
        lastRunDateTime = LocalDateTime.now();
        healthResults = healthCheckRegistry.runHealthChecks();
        logHealthResults();
        sendEvents();
    }

    private void logHealthResults() {
        HealthResult healthResult = getHealthResult();
        if (healthResult.getStatus().isHealthy()) {
            log.debug("Health checks have been updated. Current status is: healthy");
        } else {
            log.error("Health checks have been updated. Current status is: unhealthy");
        }
        healthResults.keySet().forEach(this::logHealthResult);
    }

    private void logHealthResult(String healthCheckName) {
        Result result = healthResults.get(healthCheckName);
        if (result.isHealthy()) {
            log.debug("Health check '{}' is healthy", healthCheckName);
        } else {
            log.error("Health check '{}' is unhealthy with message: '{}' and details: {}",
                healthCheckName, result.getMessage(), result.getDetails(), result.getError());
        }
    }

    private void sendEvents() {
        if (!getHealthResult().getStatus().isHealthy()) {
            eventBus.post(new ApplicationGetsUnhealthyEvent());
        }
    }

    public HealthResult getHealthResult() {
        return HealthResult.builder()
            .lastCheckedAt(lastRunDateTime)
            .status(resolveStatus())
            .build();
    }

    private HealthStatus resolveStatus() {
        boolean healthy = healthResults.values().stream().allMatch(Result::isHealthy);
        return HealthStatus.getStatus(healthy);
    }

}
