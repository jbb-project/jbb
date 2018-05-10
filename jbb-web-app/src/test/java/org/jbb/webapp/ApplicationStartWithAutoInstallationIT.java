/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.lib.test.MockCommonsAutoInstallConfig;
import org.jbb.system.api.health.HealthCheckService;
import org.jbb.system.api.health.HealthStatus;
import org.jbb.system.api.install.InstallationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MockCommonsAutoInstallConfig.class,
        LibsCompositeConfig.class, DomainCompositeConfig.class,
        WebCompositeConfig.class, RestCompositeConfig.class})
@WebAppConfiguration
public class ApplicationStartWithAutoInstallationIT {
    @Autowired
    private ApplicationContext context;

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
