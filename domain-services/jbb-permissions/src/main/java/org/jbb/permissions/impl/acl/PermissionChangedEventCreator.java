/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.acl;

import org.jbb.lib.eventbus.JbbEvent;
import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.api.matrix.PermissionMatrix;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.event.AdministratorPermissionChangedEvent;
import org.jbb.permissions.event.MemberPermissionChangedEvent;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

import static org.jbb.permissions.api.identity.SecurityIdentity.Type.MEMBER;
import static org.jbb.permissions.api.permission.PermissionType.ADMINISTRATOR_PERMISSIONS;
import static org.jbb.permissions.api.permission.PermissionType.MEMBER_PERMISSIONS;

@Component
public class PermissionChangedEventCreator {

    public static final String TYPE_NOT_FOUND = "Permission type is not found";

    private Predicate<PermissionType> isAdministrator = permissionType -> permissionType.equals(ADMINISTRATOR_PERMISSIONS);
    private Predicate<PermissionType> isMember = permissionType -> permissionType.equals(MEMBER_PERMISSIONS);

    public JbbEvent create(PermissionMatrix matrix) {
        PermissionType permissionType = matrix.getPermissionType();
        SecurityIdentity securityIdentity = matrix.getSecurityIdentity();

        Long memberId = securityIdentity.getType() == MEMBER ? securityIdentity.getId() : null;
        String identityGroupName = securityIdentity.getType() != MEMBER ? securityIdentity.getType().name() : null;

        if (isAdministrator.test(permissionType)) {
            return new AdministratorPermissionChangedEvent(memberId, identityGroupName);
        } else if (isMember.test(permissionType)) {
            return new MemberPermissionChangedEvent(memberId, identityGroupName);
        }

        throw new IllegalStateException(TYPE_NOT_FOUND);
    }
}
