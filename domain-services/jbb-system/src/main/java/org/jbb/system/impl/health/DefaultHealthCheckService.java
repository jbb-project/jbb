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

import lombok.RequiredArgsConstructor;
import org.jbb.system.api.health.HealthCheckService;
import org.jbb.system.api.health.HealthResult;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultHealthCheckService implements HealthCheckService {

    private final HealthCheckManager healthCheckManager;

    @Override
    public HealthResult getHealth() {
        return healthCheckManager.getHealthResult();
    }

    @Override
    public void forceUpdateHealth() {
        healthCheckManager.runHealthChecks();
    }

}
