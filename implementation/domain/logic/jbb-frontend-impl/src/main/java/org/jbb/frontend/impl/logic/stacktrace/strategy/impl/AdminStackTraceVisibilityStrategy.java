/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.logic.stacktrace.strategy.impl;

import org.jbb.frontend.api.service.stacktrace.StackTraceVisibilityUsersValues;
import org.jbb.frontend.impl.logic.stacktrace.strategy.api.StackTraceStrategy;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Component
public class AdminStackTraceVisibilityStrategy implements StackTraceStrategy {
    @Override
    public boolean canHandle(StackTraceVisibilityUsersValues visibilityLevel, Principal principal) {
        return false;
    }

    @Override
    public Optional<String> getStackTraceString(Throwable ex) {
        return null;
    }
}
