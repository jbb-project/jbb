/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.model.logging;

import org.jbb.system.api.service.LoggingSettingsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class AppLoggerNameUniqueValidatorTest {
    @Mock
    private LoggingSettingsService loggingSettingsServiceMock;

    @InjectMocks
    private AppLoggerNameUniqueValidator validator;

    @Test
    public void shouldValidationPassed_whenLoggerWithGivenNameDoNotExist() throws Exception {
        // given
        String name = "org.jbb";
        given(loggingSettingsServiceMock.getLogger(Matchers.eq(name))).willReturn(Optional.empty());

        // when
        boolean result = validator.isValid(name, null);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldValidationFailed_whenLoggerWithGivenNameExists() throws Exception {
        // given
        String name = "org.jbb";
        given(loggingSettingsServiceMock.getLogger(Matchers.eq(name))).willReturn(Optional.of(mock(AppLogger.class)));

        // when
        boolean result = validator.isValid(name, null);

        // then
        assertThat(result).isFalse();
    }
}