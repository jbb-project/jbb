/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.stacktrace.logic.format;

import com.google.common.base.Throwables;

import org.jbb.system.api.stacktrace.StackTraceVisibilityLevel;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(3)
public class OnlyAuthenticatedUsersCanSeeStackTraceStrategy implements StackTraceStringFormatterStrategy {
    private static final String ANONYMOUS_ROLE_NAME = "ROLE_ANONYMOUS";

    private static boolean isUserOfApplication(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .noneMatch(grantedAuthority -> ANONYMOUS_ROLE_NAME.equals(grantedAuthority.getAuthority()));
    }

    @Override
    public boolean canHandle(StackTraceVisibilityLevel visibilityLevel, UserDetails userDetails) {
        return visibilityLevel == StackTraceVisibilityLevel.USERS
                && isUserOfApplication(userDetails);
    }

    @Override
    public Optional<String> getStackTraceAsString(Throwable ex) {
        return Optional.of(Throwables.getStackTraceAsString(ex));
    }

}
