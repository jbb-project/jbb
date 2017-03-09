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

import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class AppLogger {
    public static final String ROOT_LOGGER_NAME = "ROOT";
    private static final String ID_PATTERN = "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";
    @Pattern(regexp = ID_PATTERN + "(\\." + ID_PATTERN + ")*",
            message = "{org.jbb.system.api.model.logging.AppLogger.name.pattern.message}")
    @NotBlank
    @AppLoggerNameUnique(groups = AddingModeGroup.class)
    private String name;

    @NotNull
    private LogLevel level;

    private boolean addivity;

    @NotNull
    private List<LogAppender> appenders = new ArrayList<>();

    public boolean isRootLogger() {
        return ROOT_LOGGER_NAME.equalsIgnoreCase(name);
    }

}
