/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl;


import com.google.common.collect.Lists;

import org.jbb.frontend.impl.logic.stacktrace.strategy.AdminStackTraceVisibilityStrategy;
import org.jbb.frontend.impl.logic.stacktrace.strategy.EveryoneStackTraceVisibilityStrategy;
import org.jbb.frontend.impl.logic.stacktrace.strategy.NobodyStackTraceVisibilityStrategy;
import org.jbb.frontend.impl.logic.stacktrace.strategy.StackTraceStrategy;
import org.jbb.frontend.impl.logic.stacktrace.strategy.UserStackTraceVisibilityStrategy;
import org.jbb.frontend.impl.properties.FrontendProperties;
import org.jbb.lib.properties.ModulePropertiesFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan("org.jbb.frontend.impl")
public class FrontendConfig {

    @Bean
    public FrontendProperties frontendProperties(ModulePropertiesFactory propertiesFactory) {
        return propertiesFactory.create(FrontendProperties.class);
    }

    @Bean
    public List<StackTraceStrategy> visibilityStrategies(NobodyStackTraceVisibilityStrategy nobodyStackTraceVisibilityStrategy,
                                                         AdminStackTraceVisibilityStrategy adminStackTraceVisibilityStrategy,
                                                         UserStackTraceVisibilityStrategy userStackTraceVisibilityStrategy,
                                                         EveryoneStackTraceVisibilityStrategy everyoneStackTraceVisibilityStrategy) {

        return Lists.newArrayList(nobodyStackTraceVisibilityStrategy,
                adminStackTraceVisibilityStrategy,
                userStackTraceVisibilityStrategy,
                everyoneStackTraceVisibilityStrategy);
    }
}
