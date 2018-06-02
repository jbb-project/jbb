/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest;

import org.jbb.system.api.health.HealthCheckService;
import org.jbb.system.api.install.InstallationService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SystemMockConfig {

    @Bean
    @Primary
    public HealthCheckService healthCheckService() {
        return Mockito.mock(HealthCheckService.class);
    }

    @Bean
    @Primary
    public InstallationService installationService() {
        return Mockito.mock(InstallationService.class);
    }

}
