/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.stacktrace.format;

import org.jbb.system.api.stacktrace.StackTraceVisibilityLevel;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(1)
public class NobodyCanSeeStackTraceStrategy implements StackTraceStringFormatterStrategy {
    @Override
    public boolean canHandle(StackTraceVisibilityLevel visibilityLevel, UserDetails userDetails) {
        return visibilityLevel == StackTraceVisibilityLevel.NOBODY;
    }

    @Override
    public Optional<String> getStackTraceAsString(Throwable ex) {
        return Optional.empty();
    }

}
