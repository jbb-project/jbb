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

import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.impl.acl.PermissionTypeTranslator;
import org.jbb.permissions.impl.acl.model.AclPermissionTypeEntity;
import org.jbb.permissions.impl.role.dao.AclRoleRepository;
import org.jbb.permissions.impl.role.model.AclRoleEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleTranslator {

    private final PermissionTypeTranslator permissionTypeTranslator;
    private final AclRoleRepository aclRoleRepository;


    public PermissionRoleDefinition toApiModel(AclRoleEntity roleEntity) {
        return PermissionRoleDefinition.builder()
                .id(roleEntity.getId())
                .name(roleEntity.getName())
                .description(roleEntity.getDescription())
                .permissionType(permissionTypeTranslator.toApiModel(roleEntity.getPermissionType()))
                .predefinedRole(Optional.ofNullable(roleEntity.getPredefinedRole()))
                .position(roleEntity.getPosition())
                .build();
    }

    public AclRoleEntity toNewEntity(PermissionRoleDefinition role) {
        AclPermissionTypeEntity permissionType = permissionTypeTranslator
                .toEntity(role.getPermissionType());

        Integer targetPosition = aclRoleRepository
                .findTopByPermissionTypeOrderByPositionDesc(permissionType)
                .map(foundRole -> foundRole.getPosition() + 1)
                .orElse(1);

        return AclRoleEntity.builder()
                .name(role.getName())
                .description(role.getDescription())
                .permissionType(permissionType)
                .position(targetPosition)
                .predefinedRole(role.getPredefinedRole().orElse(null))
                .build();
    }

    public Optional<AclRoleEntity> toEntity(PermissionRoleDefinition role) {
        return Optional.ofNullable(aclRoleRepository.findOne(role.getId()));
    }

}
