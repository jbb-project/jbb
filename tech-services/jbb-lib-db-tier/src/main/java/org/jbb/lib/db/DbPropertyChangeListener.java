/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.db;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DbPropertyChangeListener implements PropertyChangeListener {
    private final CloseableProxyDataSource proxyDataSource;
    private final DataSourceFactoryBean dataSourceFactoryBean;

    private final JbbEntityManagerFactory jbbEntityManagerFactory;
    private final ProxyEntityManagerFactory proxyEntityManagerFactory;

    private final SpringLiquibase springLiquibase;

    private final H2ManagedTcpServerManager h2ManagedTcpServerManager;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        try {
            proxyDataSource.close();
            h2ManagedTcpServerManager.stopH2Server();
        } catch (Exception e) {
            log.warn("Closing data source failed", e);
        }

        h2ManagedTcpServerManager.afterPropertiesSet();
        proxyDataSource.setDataSource(dataSourceFactoryBean.getObject());
        try {
            springLiquibase.afterPropertiesSet();
        } catch (LiquibaseException e) {
            log.warn("Liquibase failed", e);
        }
        LocalContainerEntityManagerFactoryBean newEmFactory = jbbEntityManagerFactory.getNewInstance();
        newEmFactory.afterPropertiesSet();
        proxyEntityManagerFactory.setObjectBeingProxied(newEmFactory.getObject());

    }
}
