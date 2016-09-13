/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.stacktrace.logic;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.Validate;
import org.jbb.frontend.api.data.StackTraceVisibilityLevel;
import org.jbb.frontend.api.service.StackTraceService;
import org.jbb.frontend.impl.base.properties.FrontendProperties;
import org.jbb.frontend.impl.stacktrace.data.UserDetailsSource;
import org.jbb.frontend.impl.stacktrace.logic.format.StackTraceStringFormatterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StackTraceServiceImpl implements StackTraceService {
    private static final Optional<String> STACKTRACE_HIDDEN = Optional.empty();

    private final FrontendProperties properties;
    private final UserDetailsSource userDetailsSource;
    private final List<StackTraceStringFormatterStrategy> stackTraceStringFormatterStrategyList;

    @Autowired
    public StackTraceServiceImpl(FrontendProperties properties,
                                 UserDetailsSource userDetailsSource,
                                 List<StackTraceStringFormatterStrategy> stackTraceStringFormatterStrategyList) {
        this.properties = properties;
        this.userDetailsSource = userDetailsSource;
        this.stackTraceStringFormatterStrategyList = stackTraceStringFormatterStrategyList;
    }

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

    private StackTraceVisibilityLevel readStackTraceVisibilityProperty() {
        return EnumUtils.getEnum(StackTraceVisibilityLevel.class, properties.stackTraceVisibilityLevel().toUpperCase());
    }

}
