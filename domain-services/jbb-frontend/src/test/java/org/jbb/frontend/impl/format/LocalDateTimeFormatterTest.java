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

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LocalDateTimeFormatterTest {
    @Mock
    private FrontendProperties propertiesMock;

    @Before
    public void setUp() throws Exception {
        when(propertiesMock.localDateTimeFormatPattern()).thenReturn("dd/MM/yyyy HH:mm:ss");
    }

    @Test
    public void shouldFormatLocalDateTimeToString() throws Exception {
        // given
        LocalDateTime dateTime = LocalDateTime.of(2016, 12, 19, 17, 5, 22);

        // when
        LocalDateTimeFormatter formatter = new LocalDateTimeFormatter(propertiesMock);
        String dateTimeString = formatter.print(dateTime, Locale.getDefault());

        // then
        assertThat(dateTimeString).isEqualTo("19/12/2016 17:05:22");
    }

    @Test
    public void shouldParseStringToLocalDateTime() throws Exception {
        // given
        String dateTimeString = "19/12/2016 17:05:22";
        LocalDateTime expectedDateTime = LocalDateTime.of(2016, 12, 19, 17, 5, 22);

        // when
        LocalDateTimeFormatter formatter = new LocalDateTimeFormatter(propertiesMock);
        LocalDateTime dateTime = formatter.parse(dateTimeString, Locale.getDefault());

        // then
        assertThat(dateTime).isEqualTo(expectedDateTime);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullPatternHandled() throws Exception {
        // when
        LocalDateTimeFormatter formatter = new LocalDateTimeFormatter(propertiesMock);
        formatter.setPattern(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenEmptyPatternHandled() throws Exception {
        // when
        LocalDateTimeFormatter formatter = new LocalDateTimeFormatter(propertiesMock);
        formatter.setPattern(StringUtils.EMPTY);

        // then
        // throw IllegalArgumentException
    }

    @Test
    public void shouldUpdateProperty_whenSettingPatternInvoked() throws Exception {
        // given
        String pattern = "dd.MM.yyyy HH:mm:ss";

        // when
        LocalDateTimeFormatter formatter = new LocalDateTimeFormatter(propertiesMock);
        formatter.setPattern(pattern);

        // then
        verify(propertiesMock, times(1))
                .setProperty(eq(FrontendProperties.LOCAL_DATE_TIME_FORMAT_KEY), eq(pattern));

    }
}