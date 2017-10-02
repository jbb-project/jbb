/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl;

import com.google.common.collect.Lists;
import java.util.List;
import org.jbb.lib.properties.ModulePropertiesFactory;
import org.jbb.system.impl.base.properties.SystemProperties;
import org.jbb.system.impl.session.logic.SessionMaxInactiveTimeChangeListener;
import org.jbb.system.impl.stacktrace.logic.format.EverybodyCanSeeStackTraceStrategy;
import org.jbb.system.impl.stacktrace.logic.format.NobodyCanSeeStackTraceStrategy;
import org.jbb.system.impl.stacktrace.logic.format.OnlyAdministratorsCanSeeStackTraceStrategy;
import org.jbb.system.impl.stacktrace.logic.format.OnlyAuthenticatedUsersCanSeeStackTraceStrategy;
import org.jbb.system.impl.stacktrace.logic.format.StackTraceStringFormatterStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

@Configuration
@ComponentScan("org.jbb.system.impl")
@EnableSpringHttpSession
@EnableScheduling
public class SystemConfig {
    @Bean
    public SystemProperties systemProperties(ModulePropertiesFactory propertiesFactory,
                                             SessionMaxInactiveTimeChangeListener sessionMaxInactiveTimeChangeListener) {
        SystemProperties systemProperties = propertiesFactory.create(SystemProperties.class);
        systemProperties.addPropertyChangeListener(
            SystemProperties.SESSION_INACTIVE_INTERVAL_TIME_SECONDS_KEY,
                sessionMaxInactiveTimeChangeListener
        );
        return systemProperties;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public List<StackTraceStringFormatterStrategy> visibilityStrategies(NobodyCanSeeStackTraceStrategy nobodyCanSeeStackTraceStrategy,
                                                                        OnlyAdministratorsCanSeeStackTraceStrategy onlyAdministratorsCanSeeStackTraceStrategy,
                                                                        OnlyAuthenticatedUsersCanSeeStackTraceStrategy onlyAuthenticatedUsersCanSeeStackTraceStrategy,
                                                                        EverybodyCanSeeStackTraceStrategy everybodyCanSeeStackTraceStrategy) {

        return Lists.newArrayList(nobodyCanSeeStackTraceStrategy,
                onlyAdministratorsCanSeeStackTraceStrategy,
                onlyAuthenticatedUsersCanSeeStackTraceStrategy,
                everybodyCanSeeStackTraceStrategy);
    }

}
