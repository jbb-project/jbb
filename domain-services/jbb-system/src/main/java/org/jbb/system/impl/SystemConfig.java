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

import org.jbb.lib.db.DbConfig;
import org.jbb.lib.properties.ModulePropertiesFactory;
import org.jbb.system.impl.session.SessionMaxInactiveTimeChangeListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(
    basePackages = {"org.jbb.system.impl.install.dao"},
    entityManagerFactoryRef = DbConfig.EM_FACTORY_BEAN_NAME,
    transactionManagerRef = DbConfig.JPA_MANAGER_BEAN_NAME)
@EnableTransactionManagement
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

}
