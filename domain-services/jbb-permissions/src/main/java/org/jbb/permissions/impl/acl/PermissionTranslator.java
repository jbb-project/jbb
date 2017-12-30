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

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.jbb.permissions.api.entry.PermissionValue;
import org.jbb.permissions.api.permission.Permission;
import org.jbb.permissions.api.permission.PermissionDefinition;
import org.jbb.permissions.api.permission.domain.AdministratorPermissions;
import org.jbb.permissions.api.permission.domain.MemberPermissions;
import org.jbb.permissions.impl.acl.dao.AclPermissionRepository;
import org.jbb.permissions.impl.acl.model.AclPermissionEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PermissionTranslator {

    private final AclPermissionRepository aclPermissionRepository;

    public Permission toApiModel(AclPermissionEntity permissionEntity, PermissionValue value) {
        String code = permissionEntity.getCode();

        PermissionDefinition definition = null;
        if (code.startsWith(AdministratorPermissions.ADMIN_ROLE_PREFIX)) {
            definition = EnumUtils.getEnum(AdministratorPermissions.class,
                    StringUtils.substringAfter(code, AdministratorPermissions.ADMIN_ROLE_PREFIX));
        } else if (code.startsWith(MemberPermissions.MEMBER_ROLE_PREFIX)) {
            definition = EnumUtils.getEnum(MemberPermissions.class,
                    StringUtils.substringAfter(code, MemberPermissions.MEMBER_ROLE_PREFIX));
        }

        return Permission.builder()
                .definition(definition)
                .value(value)
                .build();
    }

    public AclPermissionEntity toEntity(Permission permission) {
        return aclPermissionRepository.findAllByCode(permission.getDefinition().getCode());
    }

}
