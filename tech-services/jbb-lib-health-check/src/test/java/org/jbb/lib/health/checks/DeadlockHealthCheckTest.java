/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.health.checks;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DeadlockHealthCheckTest {

    @Mock
    private ThreadDeadlockHealthCheck threadDeadlockHealthCheckMock;

    @InjectMocks
    private DeadlockHealthCheck deadlockHealthCheck;

    @Before
    public void injectMock() {
        Field threadDeadlockHealthCheckField = ReflectionUtils.findField(DeadlockHealthCheck.class, "threadDeadlockHealthCheck");
        ReflectionUtils.makeAccessible(threadDeadlockHealthCheckField);
        ReflectionUtils.setField(threadDeadlockHealthCheckField, deadlockHealthCheck, threadDeadlockHealthCheckMock);
    }

    @Test
    public void shouldReturnNotEmptyName() {
        // when
        String checkName = deadlockHealthCheck.getName();

        // then
        assertThat(checkName).isNotBlank();
    }

    @Test
    public void shouldBeHealthy_whenDownstreamHealthCheckIsHealthy() throws Exception {
        // given
        given(threadDeadlockHealthCheckMock.execute()).willReturn(HealthCheck.Result.healthy());

        // when
        HealthCheck.Result result = deadlockHealthCheck.check();

        // then
        assertThat(result.isHealthy()).isTrue();
    }

    @Test
    public void shouldBeUnealthy_whenDownstreamHealthCheckIsUnhealthy() throws Exception {
        // given
        given(threadDeadlockHealthCheckMock.execute()).willReturn(HealthCheck.Result.unhealthy("not healthy"));

        // when
        HealthCheck.Result result = deadlockHealthCheck.check();

        // then
        assertThat(result.isHealthy()).isFalse();
    }

}