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
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.properties.ModulePropertiesFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@Configuration
@EnableJpaRepositories(
        basePackages = {"org.jbb.frontend.impl.ucp.dao"},
        entityManagerFactoryRef = DbConfig.EM_FACTORY_BEAN_NAME,
        transactionManagerRef = DbConfig.JTA_MANAGER_BEAN_NAME)
@EnableTransactionManagement
@ComponentScan("org.jbb.frontend.impl")
public class FrontendConfig {

    @Bean
    public FrontendProperties frontendProperties(ModulePropertiesFactory propertiesFactory) {
        return propertiesFactory.create(FrontendProperties.class);
    }

    @Bean
    public List<StackTraceStrategy> visibilityStrategies(AdminStackTraceVisibilityStrategy adminStackTraceVisibilityStrategy,
                                                         EveryoneStackTraceVisibilityStrategy everyoneStackTraceVisibilityStrategy,
                                                         NobodyStackTraceVisibilityStrategy nobodyStackTraceVisibilityStrategy,
                                                         UserStackTraceVisibilityStrategy userStackTraceVisibilityStrategy) {

        return Lists.newArrayList(nobodyStackTraceVisibilityStrategy, adminStackTraceVisibilityStrategy, userStackTraceVisibilityStrategy, everyoneStackTraceVisibilityStrategy);
    }
}
