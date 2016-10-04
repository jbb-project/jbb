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

import org.jbb.frontend.impl.base.properties.FrontendProperties;
import org.jbb.frontend.impl.stacktrace.logic.format.EverybodyCanSeeStackTraceStrategy;
import org.jbb.frontend.impl.stacktrace.logic.format.NobodyCanSeeStackTraceStrategy;
import org.jbb.frontend.impl.stacktrace.logic.format.OnlyAdministratorsCanSeeStackTraceStrategy;
import org.jbb.frontend.impl.stacktrace.logic.format.OnlyAuthenticatedUsersCanSeeStackTraceStrategy;
import org.jbb.frontend.impl.stacktrace.logic.format.StackTraceStringFormatterStrategy;
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
        basePackages = {"org.jbb.frontend.impl.ucp.dao", "org.jbb.frontend.impl.acp.dao"},
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
