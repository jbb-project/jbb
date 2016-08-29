/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.services;

import org.jbb.frontend.api.services.FormatterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Locale;

public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {
    private final FormatterService formatterService;

    @Autowired
    public LocalDateTimeFormatter(FormatterService formatterService) {
        this.formatterService = formatterService;
    }

    @Override
    public LocalDateTime parse(String text, Locale locale) throws ParseException {
        return LocalDateTime.parse(text, formatterService.localDateTimeFormatter());
    }

    @Override
    public String print(LocalDateTime object, Locale locale) {
        return object.format(formatterService.localDateTimeFormatter());
    }
}