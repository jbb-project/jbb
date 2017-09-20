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

import static org.jbb.permissions.api.permission.member.MemberPermissionCategory.MISC;
import static org.jbb.permissions.api.permission.member.MemberPermissionCategory.PROFILE;

import org.jbb.permissions.api.permission.PermissionCategory;
import org.jbb.permissions.api.permission.PermissionDefinition;

public enum MemberPermissions implements PermissionDefinition {
    // Profile permissions
    CAN_CHANGE_EMAIL("Can change email", PROFILE, 1),
    CAN_CHANGE_DISPLAYED_NAME("Can change displayed name", PROFILE, 2),

    // Misc permissions
    CAN_VIEW_FAQ("Can view faq", MISC, 1),;

    private final String name;
    private final PermissionCategory category;
    private final Integer position;

    MemberPermissions(String name, PermissionCategory category, Integer position) {
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
