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

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.matrix.PermissionTable.Builder;
import org.jbb.permissions.impl.role.model.AclRoleEntryEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionTableTranslator {

    private final PermissionTranslator permissionTranslator;

    public PermissionTable toApiModel(List<AclRoleEntryEntity> roleEntries) {
        Builder builder = PermissionTable.builder();
        roleEntries.forEach(roleEntry -> builder.putPermission(
            permissionTranslator.toApiModel(roleEntry.getPermission(), roleEntry.getEntryValue())
        ));
        return builder.build();
    }

}
