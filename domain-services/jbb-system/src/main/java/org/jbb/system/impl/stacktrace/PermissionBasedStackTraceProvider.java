/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.stacktrace;

import static org.jbb.permissions.api.permission.domain.MemberPermissions.CAN_SEE_STACKTRACE;

import com.google.common.base.Throwables;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.jbb.lib.commons.web.ClientStackTraceProvider;
import org.jbb.permissions.api.PermissionService;

@RequiredArgsConstructor
public class PermissionBasedStackTraceProvider implements ClientStackTraceProvider {

    private final PermissionService permissionService;

    @Override
    public Optional<String> getClientStackTrace(Exception ex) {
        Validate.notNull(ex);
        if (permissionService.checkPermission(CAN_SEE_STACKTRACE)) {
            return Optional.of(Throwables.getStackTraceAsString(ex));
        }
        return Optional.empty();
    }
}
