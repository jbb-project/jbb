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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseHealthCheckTest {

    @Mock
    private DataSource dataSourceMock;

    @Mock
    private JdbcTemplate jdbcTemplateMock;

    @InjectMocks
    private DatabaseHealthCheck databaseHealthCheck;

    @Before
    public void callPostConstruct() {
        jdbcTemplateMock = Mockito.mock(JdbcTemplate.class);
        Field jdbcTemplateField = ReflectionUtils.findField(DatabaseHealthCheck.class, "jdbcTemplate");
        ReflectionUtils.makeAccessible(jdbcTemplateField);
        ReflectionUtils.setField(jdbcTemplateField, databaseHealthCheck, jdbcTemplateMock);
    }

    @Test
    public void shouldReturnNotEmptyName() {
        // when
        String checkName = databaseHealthCheck.getName();

        // then
        assertThat(checkName).isNotBlank();
    }

    @Test
    public void shouldBeHealthy_whenQuerySucceeds() throws Exception {
        // when
        HealthCheck.Result result = databaseHealthCheck.check();

        // then
        assertThat(result.isHealthy()).isTrue();
    }

    @Test
    public void shouldBeUnhealthy_whenQueryFail() throws Exception {
        // given
        given(jdbcTemplateMock.query(any(String.class), any(RowMapper.class))).willThrow(new JpaSystemException(new RuntimeException()));
        // when
        HealthCheck.Result result = databaseHealthCheck.check();

        // then
        assertThat(result.isHealthy()).isFalse();
    }


}