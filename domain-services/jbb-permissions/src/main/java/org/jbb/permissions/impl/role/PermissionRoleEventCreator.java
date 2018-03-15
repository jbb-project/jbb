/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.role;

import org.jbb.lib.eventbus.JbbEvent;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.event.AdministratorPermissionRoleChangedEvent;
import org.jbb.permissions.event.AdministratorPermissionRoleCreatedEvent;
import org.jbb.permissions.event.AdministratorPermissionRoleRemovedEvent;
import org.jbb.permissions.event.MemberPermissionRoleChangedEvent;
import org.jbb.permissions.event.MemberPermissionRoleCreatedEvent;
import org.jbb.permissions.event.MemberPermissionRoleRemovedEvent;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

import static org.jbb.permissions.api.permission.PermissionType.ADMINISTRATOR_PERMISSIONS;
import static org.jbb.permissions.api.permission.PermissionType.MEMBER_PERMISSIONS;

@Component
public class PermissionRoleEventCreator {
    public static final String TYPE_NOT_FOUND = "Permission type is not found";

    private Predicate<PermissionType> isAdministrator = permissionType -> permissionType.equals(ADMINISTRATOR_PERMISSIONS);
    private Predicate<PermissionType> isMember = permissionType -> permissionType.equals(MEMBER_PERMISSIONS);

    public JbbEvent createRoleCreatedEvent(PermissionType permissionType, Long roleId) {
        if (isAdministrator.test(permissionType)) {
            return new AdministratorPermissionRoleCreatedEvent(roleId);
        } else if (isMember.test(permissionType)) {
            return new MemberPermissionRoleCreatedEvent(roleId);
        }
        throw new IllegalStateException(TYPE_NOT_FOUND);
    }

    public JbbEvent createRoleChangedEvent(PermissionType permissionType, Long roleId) {
        if (isAdministrator.test(permissionType)) {
            return new AdministratorPermissionRoleChangedEvent(roleId);
        } else if (isMember.test(permissionType)) {
            return new MemberPermissionRoleChangedEvent(roleId);
        }
        throw new IllegalStateException(TYPE_NOT_FOUND);
    }

    public JbbEvent createRoleRemovedEvent(PermissionType permissionType, Long roleId) {
        if (isAdministrator.test(permissionType)) {
            return new AdministratorPermissionRoleRemovedEvent(roleId);
        } else if (isMember.test(permissionType)) {
            return new MemberPermissionRoleRemovedEvent(roleId);
        }
        throw new IllegalStateException(TYPE_NOT_FOUND);
    }
}
