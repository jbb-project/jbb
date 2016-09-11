/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.logic.stacktrace;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.Validate;
import org.jbb.frontend.api.service.stacktrace.StackTraceVisibilityUsersService;
import org.jbb.frontend.api.service.stacktrace.StackTraceVisibilityUsersValues;
import org.jbb.frontend.impl.logic.stacktrace.strategy.StackTraceStrategy;
import org.jbb.frontend.impl.properties.FrontendProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StackTraceVisibilityUsersServiceImpl implements StackTraceVisibilityUsersService {
    private static final Optional<String> STACKTRACE_HIDDEN = Optional.empty();

    private final FrontendProperties properties;
    private final UserDetailsExtractor userDetailsExtractor;
    private final List<StackTraceStrategy> stackTraceStrategyList;

    @Autowired
    public StackTraceVisibilityUsersServiceImpl(FrontendProperties properties,
                                                UserDetailsExtractor userDetailsExtractor,
                                                List<StackTraceStrategy> stackTraceStrategyList) {
        this.properties = properties;
        this.userDetailsExtractor = userDetailsExtractor;
        this.stackTraceStrategyList = stackTraceStrategyList;
    }

    @Override
    public Optional<String> getPermissionToStackTraceVisibility(Exception ex) {
        Validate.notNull(ex);

        for (StackTraceStrategy strategy : stackTraceStrategyList) {
            StackTraceVisibilityUsersValues visibilityProperty = readStackTraceVisibilityProperty();
            UserDetails userDetails = userDetailsExtractor.getUserDetailsFromApplicationContext();

            if (strategy.canHandle(visibilityProperty, userDetails)) {
                return strategy.getStackTraceString(ex);
            }
        }

        return STACKTRACE_HIDDEN;
    }

    private StackTraceVisibilityUsersValues readStackTraceVisibilityProperty() {
        return EnumUtils.getEnum(StackTraceVisibilityUsersValues.class, properties.stackTraceVisibilityUsers().toUpperCase());
    }

}
