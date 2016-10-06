/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl;

import com.google.common.collect.Lists;

import org.jbb.lib.properties.ModulePropertiesFactory;
import org.jbb.system.impl.base.properties.SystemProperties;
import org.jbb.system.impl.stacktrace.logic.format.EverybodyCanSeeStackTraceStrategy;
import org.jbb.system.impl.stacktrace.logic.format.NobodyCanSeeStackTraceStrategy;
import org.jbb.system.impl.stacktrace.logic.format.OnlyAdministratorsCanSeeStackTraceStrategy;
import org.jbb.system.impl.stacktrace.logic.format.OnlyAuthenticatedUsersCanSeeStackTraceStrategy;
import org.jbb.system.impl.stacktrace.logic.format.StackTraceStringFormatterStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan("org.jbb.system.impl")
public class SystemConfig {
    @Bean
    public SystemProperties frontendProperties(ModulePropertiesFactory propertiesFactory) {
        return propertiesFactory.create(SystemProperties.class);
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
