/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.db.metrics;

import com.google.common.collect.Lists;

import org.jbb.lib.db.DbConfig;
import org.jbb.lib.metrics.MetricsGroup;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;

import io.micrometer.core.instrument.binder.jpa.HibernateMetrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JdbcMetricsGroup implements MetricsGroup {

    private final DataSourcePoolMetrics dataSourcePoolMetrics;
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public void registerMetrics(CompositeMeterRegistry meterRegistry) {
        dataSourcePoolMetrics.bindTo(meterRegistry);
        new HibernateMetrics(entityManagerFactory, DbConfig.EM_FACTORY_BEAN_NAME, Lists.newArrayList()).bindTo(meterRegistry);
    }
}
