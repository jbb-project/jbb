/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp;

import org.jbb.BaseIT;
import org.jbb.system.api.health.HealthCheckService;
import org.jbb.system.api.health.HealthStatus;
import org.jbb.system.api.install.InstallationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationStartWithAutoInstallationIT extends BaseIT {

    @Autowired
    private InstallationService installationService;

    @Autowired
    private HealthCheckService healthCheckService;

    @Test
    public void shouldSpringContextStart() {
        // then
        assertThat(context.getStartupDate()).isPositive();
    }

    @Test
    public void shouldInstallationSucceed() {
        // then
        assertThat(installationService.isInstalled()).isTrue();
    }

    @Test
    public void shouldBeHealthy() {
        // then
        assertThat(healthCheckService.getHealth().getStatus()).isEqualTo(HealthStatus.HEALTHY);
    }
}
