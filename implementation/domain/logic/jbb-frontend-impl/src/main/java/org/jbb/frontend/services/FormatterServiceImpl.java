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

import org.apache.commons.lang3.Validate;
import org.jbb.frontend.api.services.FormatterService;
import org.jbb.frontend.properties.FrontendProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class FormatterServiceImpl implements FormatterService {
    private final FrontendProperties frontendProperties;

    private DateTimeFormatter formatter;

    @Autowired
    public FormatterServiceImpl(FrontendProperties frontendProperties) {
        this.frontendProperties = frontendProperties;
    }

    @Override
    public DateTimeFormatter localDateTimeFormatter() {
        formatter = DateTimeFormatter.ofPattern(frontendProperties.localDateTimeFormatPattern());
        return formatter;
    }

    @Override
    public void setLocalDateTimeFormat(String formatPattern) {
        Validate.notBlank(formatPattern);
        frontendProperties.setProperty(FrontendProperties.LOCAL_DATE_TIME_FORMAT_KEY, formatPattern);
    }
}
