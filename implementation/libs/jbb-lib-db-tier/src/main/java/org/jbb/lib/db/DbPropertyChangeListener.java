/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.db;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbPropertyChangeListener implements PropertyChangeListener {
    private final CloseableProxyDataSource proxyDataSource;
    private final DataSourceFactoryBean dataSourceFactoryBean;

    private final JbbEntityManagerFactory jbbEntityManagerFactory;

    public DbPropertyChangeListener(CloseableProxyDataSource proxyDataSource,
                                    DataSourceFactoryBean dataSourceFactoryBean,
                                    JbbEntityManagerFactory jbbEntityManagerFactory) {
        this.proxyDataSource = proxyDataSource;
        this.dataSourceFactoryBean = dataSourceFactoryBean;
        this.jbbEntityManagerFactory = jbbEntityManagerFactory;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        try {
            proxyDataSource.close();
        } catch (Exception e) {
            log.warn("Closing data source failed", e);
        }

        proxyDataSource.setDataSource(dataSourceFactoryBean.getObject());

    }
}
