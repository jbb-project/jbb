/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.api.permission.domain;

import static org.jbb.permissions.api.permission.domain.AllPermissionCategories.FORUMS;
import static org.jbb.permissions.api.permission.domain.AllPermissionCategories.MEMBERS;
import static org.jbb.permissions.api.permission.domain.AllPermissionCategories.PERMISSIONS;

import org.jbb.permissions.api.permission.PermissionCategory;
import org.jbb.permissions.api.permission.PermissionDefinition;

public enum AdministratorPermissions implements PermissionDefinition {

    // Permission permissions
    CAN_ALTER_ADMINISTRATOR_PERMISSIONS("Can alter administrator permissions", PERMISSIONS, 1),
    CAN_ALTER_MEMBER_PERMISSIONS("Can alter member permissions", PERMISSIONS, 2),
    CAN_MANAGE_PERMISSION_ROLES("Can manage permission roles", PERMISSIONS, 3),

    // Member permissions
    CAN_MANAGE_MEMBERS("Can manage members", MEMBERS, 1),
    CAN_DELETE_MEMBERS("Can delete members", MEMBERS, 2),

    // Forum permissions
    CAN_ADD_FORUMS("Can add forums", FORUMS, 1),
    CAN_MODIFY_FORUMS("Can modify forums", FORUMS, 2),
    CAN_DELETE_FORUMS("Can delete forums", FORUMS, 3);

    public static final String ADMIN_ROLE_PREFIX = "ADM_";

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
        return ADMIN_ROLE_PREFIX + this.name();
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
