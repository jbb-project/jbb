/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc.formatters;

import org.jbb.lib.mvc.properties.MvcProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LocalDateTimeFormatterTest {
    @Mock
    private MvcProperties properties;

    @Before
    public void setUp() throws Exception {
        when(properties.localDateTimeFormatPattern()).thenReturn("dd/MM/yyyy HH:mm:ss");
    }

    @Test
    public void shouldFormatLocalDateTimeToString() throws Exception {
        // given
        LocalDateTime dateTime = LocalDateTime.of(2016, 12, 19, 17, 5, 22);

        // when
        LocalDateTimeFormatter formatter = new LocalDateTimeFormatter(properties);
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
        LocalDateTimeFormatter formatter = new LocalDateTimeFormatter(properties);
        LocalDateTime dateTime = formatter.parse(dateTimeString, Locale.getDefault());

        // then
        assertThat(dateTime).isEqualTo(expectedDateTime);
    }
}