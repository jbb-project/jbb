/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.format;

import org.apache.commons.lang3.Validate;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {

    private final FrontendProperties frontendProperties;

    @Override
    public LocalDateTime parse(String text, Locale locale) throws ParseException {
        return LocalDateTime.parse(text, getCurrentDateTimeFormatter());
    }

    @Override
    public String print(LocalDateTime object, Locale locale) {
        return object.format(getCurrentDateTimeFormatter());
    }

    public DateTimeFormatter getCurrentDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(frontendProperties.localDateTimeFormatPattern());
    }

    public void setPattern(String pattern) {
        Validate.notBlank(pattern);
        DateTimeFormatter.ofPattern(pattern);
        frontendProperties.setProperty(FrontendProperties.LOCAL_DATE_TIME_FORMAT_KEY, pattern);
    }

    public String getCurrentPattern() {
        return frontendProperties.localDateTimeFormatPattern();
    }

}