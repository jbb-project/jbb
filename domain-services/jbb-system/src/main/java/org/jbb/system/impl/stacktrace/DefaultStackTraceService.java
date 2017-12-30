/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.stacktrace;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.Validate;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.system.api.stacktrace.StackTraceService;
import org.jbb.system.api.stacktrace.StackTraceVisibilityLevel;
import org.jbb.system.impl.SystemProperties;
import org.jbb.system.impl.stacktrace.format.StackTraceStringFormatterStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultStackTraceService implements StackTraceService {
    private static final Optional<String> STACKTRACE_HIDDEN = Optional.empty();

    private final SystemProperties properties;
    private final UserDetailsSource userDetailsSource;
    private final List<StackTraceStringFormatterStrategy> stackTraceStringFormatterStrategyList;

    @Override
    public Optional<String> getStackTraceAsString(Exception ex) {
        Validate.notNull(ex);

        for (StackTraceStringFormatterStrategy strategy : stackTraceStringFormatterStrategyList) {
            StackTraceVisibilityLevel visibilityLevel = readStackTraceVisibilityProperty();
            UserDetails userDetails = userDetailsSource.getFromApplicationContext();

            if (strategy.canHandle(visibilityLevel, userDetails)) {
                return strategy.getStackTraceAsString(ex);
            }
        }

        return STACKTRACE_HIDDEN;
    }

    @Override
    public StackTraceVisibilityLevel getCurrentStackTraceVisibilityLevel() {
        return readStackTraceVisibilityProperty();
    }

    @Override
    public void setStackTraceVisibilityLevel(StackTraceVisibilityLevel newVisibilityLevel) {
        Validate.notNull(newVisibilityLevel);
        properties.setProperty(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY,
                newVisibilityLevel.toString().toLowerCase());
    }

    private StackTraceVisibilityLevel readStackTraceVisibilityProperty() {
        return EnumUtils.getEnum(StackTraceVisibilityLevel.class, properties.stackTraceVisibilityLevel().toUpperCase());
    }

}
