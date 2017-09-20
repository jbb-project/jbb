/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.api.permission.member;

import org.jbb.permissions.api.permission.PermissionCategory;
import org.jbb.permissions.api.permission.PermissionDefinition;
import org.jbb.permissions.api.permission.category.UserPermissionCategory;

public enum MemberProfilePermission implements PermissionDefinition {
    CAN_CHANGE_EMAIL("Can change email"),
    CAN_CHANGE_DISPLAYED_NAME("Can change displayed name");

    private final String name;

    MemberProfilePermission(String name) {
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
        return UserPermissionCategory.PROFILE;
    }

    @Override
    public Integer getPosition() {
        return ordinal();
    }
}
