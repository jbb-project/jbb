/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.api.exceptions;

import org.jbb.permissions.api.permission.PermissionDefinition;

public class PermissionRequiredException extends RuntimeException {

    public PermissionRequiredException(PermissionDefinition permissionDefinition) {
        super(permissionDefinition.getCode() + " permission needed");
    }

}
