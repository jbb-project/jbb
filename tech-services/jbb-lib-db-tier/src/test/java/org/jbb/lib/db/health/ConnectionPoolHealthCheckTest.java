/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.db.health;

import com.codahale.metrics.health.HealthCheck;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ConnectionPoolHealthCheckTest {

    @Mock
    private DataSource dataSourceMock;

    private HikariDataSource hikariDataSourceMock;

    @InjectMocks
    private ConnectionPoolHealthCheck connectionPoolHealthCheck;

    @Before
    public void callPostConstruct() throws SQLException {
        hikariDataSourceMock = Mockito.mock(HikariDataSource.class);
        given(dataSourceMock.unwrap(any())).willReturn(hikariDataSourceMock);
        connectionPoolHealthCheck.unwrapHikariDataSource();
    }

    @Test
    public void shouldReturnNotEmptyName() {
        // when
        String checkName = connectionPoolHealthCheck.getName();

        // then
        assertThat(checkName).isNotBlank();
    }

    @Test
    public void shouldBeHealthy_whenPoolUsageIsBelow80Percent() throws Exception {
        // given
        HikariPoolMXBean hikariPoolMXBeanMock = mock(HikariPoolMXBean.class);
        given(hikariDataSourceMock.getHikariPoolMXBean()).willReturn(hikariPoolMXBeanMock);

        given(hikariPoolMXBeanMock.getActiveConnections()).willReturn(79);
        given(hikariPoolMXBeanMock.getTotalConnections()).willReturn(100);

        // when
        HealthCheck.Result result = connectionPoolHealthCheck.check();

        // then
        assertThat(result.isHealthy()).isTrue();
    }

    @Test
    public void shouldBeHealthy_whenPoolUsageIs80Percent() throws Exception {
        // given
        HikariPoolMXBean hikariPoolMXBeanMock = mock(HikariPoolMXBean.class);
        given(hikariDataSourceMock.getHikariPoolMXBean()).willReturn(hikariPoolMXBeanMock);

        given(hikariPoolMXBeanMock.getActiveConnections()).willReturn(80);
        given(hikariPoolMXBeanMock.getTotalConnections()).willReturn(100);

        // when
        HealthCheck.Result result = connectionPoolHealthCheck.check();

        // then
        assertThat(result.isHealthy()).isTrue();
    }

    @Test
    public void shouldBeUnhealthy_whenPoolUsageAbove80Percent() throws Exception {
        // given
        HikariPoolMXBean hikariPoolMXBeanMock = mock(HikariPoolMXBean.class);
        given(hikariDataSourceMock.getHikariPoolMXBean()).willReturn(hikariPoolMXBeanMock);

        given(hikariPoolMXBeanMock.getActiveConnections()).willReturn(81);
        given(hikariPoolMXBeanMock.getTotalConnections()).willReturn(100);

        // when
        HealthCheck.Result result = connectionPoolHealthCheck.check();

        // then
        assertThat(result.isHealthy()).isFalse();
    }


}