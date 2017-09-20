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

import static org.jbb.permissions.api.permission.admin.AdministratorPermissionCategory.FORUMS;
import static org.jbb.permissions.api.permission.admin.AdministratorPermissionCategory.MEMBERS;

import org.jbb.permissions.api.permission.PermissionCategory;
import org.jbb.permissions.api.permission.PermissionDefinition;

public enum AdministratorPermissions implements PermissionDefinition {
    // Member permissions
    CAN_MANAGE_MEMBERS("Can manage members", MEMBERS, 1),
    CAN_DELETE_MEMBERS("Can delete members", MEMBERS, 2),

    // Forum permissions
    CAN_ADD_FORUMS("Can add forums", FORUMS, 1),
    CAN_MODIFY_FORUMS("Can modify forums", FORUMS, 2),
    CAN_DELETE_FORUMS("Can delete forums", FORUMS, 3);

    private final String name;
    private final PermissionCategory category;
    private final Integer position;

    AdministratorPermissions(String name, PermissionCategory category, Integer position) {
        this.name = name;
        this.category = category;
        this.position = position;
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
        return category;
    }

    @Override
    public Integer getPosition() {
        return position;
    }
}
