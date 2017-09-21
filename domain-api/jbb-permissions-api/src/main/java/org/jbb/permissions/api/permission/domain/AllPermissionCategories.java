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

import static org.jbb.permissions.api.permission.PermissionType.ADMINISTRATOR_PERMISSIONS;
import static org.jbb.permissions.api.permission.PermissionType.MEMBER_PERMISSIONS;

import org.jbb.permissions.api.permission.PermissionCategory;
import org.jbb.permissions.api.permission.PermissionType;

public enum AllPermissionCategories implements PermissionCategory {

    // Administrative permission categories
    PERMISSIONS("Permissions", ADMINISTRATOR_PERMISSIONS, 1),
    MEMBERS("Members", ADMINISTRATOR_PERMISSIONS, 2),
    FORUMS("Forums", ADMINISTRATOR_PERMISSIONS, 3),

    // Member permission categories
    PROFILE("Profile", MEMBER_PERMISSIONS, 1),
    MEMBER_MISC("Misc", MEMBER_PERMISSIONS, 2);

    private final String name;
    private final PermissionType type;
    private final Integer position;


    AllPermissionCategories(String name, PermissionType type, Integer position) {
        this.name = name;
        this.type = type;
        this.position = position;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PermissionType getType() {
        return type;
    }

    @Override
    public Integer getPosition() {
        return position;
    }
}
