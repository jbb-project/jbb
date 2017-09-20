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

import org.jbb.permissions.api.permission.Permission;
import org.jbb.permissions.api.permission.PermissionCategory;
import org.jbb.permissions.api.permission.category.UserPermissionCategory;

public enum MemberMiscPermission implements Permission {
    CAN_VIEW_FAQ("Can view faq");

    private final String name;

    MemberMiscPermission(String name) {
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
        return UserPermissionCategory.MISC;
    }

    @Override
    public Integer getPosition() {
        return ordinal();
    }
}
