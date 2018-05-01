/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.role;

import org.jbb.permissions.api.permission.Permission;
import org.jbb.permissions.impl.acl.dao.AclPermissionRepository;
import org.jbb.permissions.impl.role.model.AclRoleEntity;
import org.jbb.permissions.impl.role.model.AclRoleEntryEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleEntryTranslator {

    private final AclPermissionRepository aclPermissionRepository;


    public AclRoleEntryEntity toEntity(Permission permission, AclRoleEntity roleEntity) {
        return AclRoleEntryEntity.builder()
                .role(roleEntity)
                .permission(aclPermissionRepository.findAllByCode(permission.getDefinition().getCode()))
                .entryValue(permission.getValue())
                .build();
    }

}
