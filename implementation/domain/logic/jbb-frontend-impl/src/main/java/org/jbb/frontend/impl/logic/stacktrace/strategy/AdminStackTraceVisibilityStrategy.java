/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.logic.stacktrace.strategy;

import com.google.common.base.Throwables;

import org.jbb.frontend.api.service.stacktrace.StackTraceVisibilityUsersValues;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Component
public class AdminStackTraceVisibilityStrategy implements StackTraceStrategy {

    @Override
    public boolean canHandle(StackTraceVisibilityUsersValues visibilityLevel,UserDetails principal) {
        return visibilityLevel == StackTraceVisibilityUsersValues.ADMINISTRATORS
                && isUserHasAdministratorPrivilages(principal);
    }

    @Override
    public Optional<String> getStackTraceString(Throwable ex) {
        return Optional.of(Throwables.getStackTraceAsString(ex));
    }

    private boolean isUserHasAdministratorPrivilages(UserDetails principal) {
        return principal.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().toUpperCase().equals(StackTraceVisibilityUsersValues.ADMINISTRATORS));
    }

}
