/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.health;

import com.codahale.metrics.health.HealthCheckRegistry;
import java.util.Collection;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HealthChecksRegistrar {

    private final ApplicationContext context;
    private final HealthCheckRegistry healthCheckRegistry;

    @PostConstruct
    public void registerAllHealthChecks() {
        Collection<JbbHealthCheck> healthChecks = context
            .getBeansOfType(JbbHealthCheck.class).values();
        healthChecks.forEach(hc -> healthCheckRegistry.register(hc.getName(), hc));
    }

}
