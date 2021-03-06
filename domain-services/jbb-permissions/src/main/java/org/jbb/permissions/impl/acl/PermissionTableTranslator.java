/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.acl;

import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.matrix.PermissionTable.Builder;
import org.jbb.permissions.impl.acl.model.AclEntryEntity;
import org.jbb.permissions.impl.role.model.AclRoleEntryEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PermissionTableTranslator {

    private final PermissionTranslator permissionTranslator;

    public PermissionTable fromRoleToApiModel(List<AclRoleEntryEntity> roleEntries) {
        Builder builder = PermissionTable.builder();
        roleEntries.forEach(roleEntry -> builder.putPermission(
                permissionTranslator.toApiModel(roleEntry.getPermission(), roleEntry.getEntryValue())
        ));
        return builder.build();
    }

    public PermissionTable toApiModel(List<AclEntryEntity> aclEntries) {
        Builder builder = PermissionTable.builder();
        aclEntries.forEach(aclEntry -> builder.putPermission(
                permissionTranslator.toApiModel(aclEntry.getPermission(), aclEntry.getEntryValue())
        ));
        return builder.build();
    }
}
