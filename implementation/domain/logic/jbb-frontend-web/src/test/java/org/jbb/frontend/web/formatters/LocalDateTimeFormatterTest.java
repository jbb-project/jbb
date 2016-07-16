/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.formatters;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalDateTimeFormatterTest {
    @Test
    public void shouldFormatLocalDateTimeToString() throws Exception {
        // given
        LocalDateTime dateTime = LocalDateTime.of(2016, 12, 19, 17, 5, 22);

        // when
        LocalDateTimeFormatter formatter = new LocalDateTimeFormatter();
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
        LocalDateTimeFormatter formatter = new LocalDateTimeFormatter();
        LocalDateTime dateTime = formatter.parse(dateTimeString, Locale.getDefault());

        // then
        assertThat(dateTime).isEqualTo(expectedDateTime);
    }
}