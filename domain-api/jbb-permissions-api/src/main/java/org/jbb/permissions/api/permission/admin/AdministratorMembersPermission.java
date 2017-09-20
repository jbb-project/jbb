/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.api.permission.admin;

import org.jbb.permissions.api.permission.Permission;
import org.jbb.permissions.api.permission.PermissionCategory;
import org.jbb.permissions.api.permission.category.AdministratorPermissionCategory;

public enum AdministratorMembersPermission implements Permission {
    CAN_MANAGE_MEMBERS("Can manage members"),
    CAN_DELETE_MEMBERS("Can delete members");

    private final String name;

    AdministratorMembersPermission(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCode() {
        return this.name();
    }

    @Override
    public PermissionCategory getCategory() {
        return AdministratorPermissionCategory.MEMBERS;
    }

    @Override
    public Integer getPosition() {
        return ordinal();
    }
}
