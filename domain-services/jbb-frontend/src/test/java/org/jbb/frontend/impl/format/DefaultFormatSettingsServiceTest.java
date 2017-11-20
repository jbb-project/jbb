/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.format;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.collect.Sets;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.jbb.frontend.api.format.FormatException;
import org.jbb.frontend.api.format.FormatSettings;
import org.jbb.frontend.event.FormatSettingsChangedEvent;
import org.jbb.lib.eventbus.JbbEventBus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultFormatSettingsServiceTest {

    @Mock
    private FrontendProperties propertiesMock;

    @Mock
    private LocalDateTimeFormatter localDateTimeFormatterMock;

    @Mock
    private DurationFormatter durationFormatterMock;

    @Mock
    private Validator validatorMock;

    @Mock
    private JbbEventBus eventBusMock;


    @InjectMocks
    private DefaultFormatSettingsService formatSettingsService;

    @Test
    public void shouldUseDateFormatFromFormatter() throws Exception {
        // given

        // when
        formatSettingsService.getFormatSettings();

        // then
        verify(localDateTimeFormatterMock, times(1)).getCurrentPattern();
    }

    @Test
    public void shouldUseDurationFromFormatter() throws Exception {
        // given

        // when
        formatSettingsService.getFormatSettings();

        // then
        verify(durationFormatterMock, times(1)).getPattern();
    }

    @Test
    public void shouldSentFormatSettingsChangedEvent_whenSetNewSettings() throws Exception {
        // given
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet());

        FormatSettings formatSettings = new FormatSettings();
        formatSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");
        formatSettings.setDurationFormat("HH:mm:ss");

        // when
        formatSettingsService.setFormatSettings(formatSettings);

        // then
        verify(eventBusMock).post(isA(FormatSettingsChangedEvent.class));
    }

    @Test(expected = FormatException.class)
    public void shouldThrowFormatException_whenValidationFailed() throws Exception {
        // given
        given(validatorMock.validate(any()))
            .willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        formatSettingsService.setFormatSettings(mock(FormatSettings.class));

        // then
        // throw FormatException
    }
}