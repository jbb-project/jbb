/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.api.role;

import org.jbb.permissions.api.permission.PermissionType;

public enum AdministratorPermissionRole implements PermissionRole {
    STANDARD_ADMIN("Standard administrator"),
    FORUM_ADMIN("Forum administrator");

    private String name;

    AdministratorPermissionRole(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PermissionType getPermissionType() {
        return PermissionType.ADMINISTRATOR_PERMISSIONS;
    }

    @Override
    public Integer getPosition() {
        return ordinal();
    }
}
