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

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.Permission;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.impl.acl.PermissionTypeEntityResolver;
import org.jbb.permissions.impl.acl.dao.AclPermissionRepository;
import org.jbb.permissions.impl.acl.model.AclPermissionTypeEntity;
import org.jbb.permissions.impl.role.dao.AclRoleEntryRepository;
import org.jbb.permissions.impl.role.dao.AclRoleRepository;
import org.jbb.permissions.impl.role.model.AclRoleEntity;
import org.jbb.permissions.impl.role.model.AclRoleEntryEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionRoleServiceImpl implements PermissionRoleService {

    private final AclRoleRepository aclRoleRepository;
    private final AclPermissionRepository aclPermissionRepository;
    private final AclRoleEntryRepository aclRoleEntryRepository;
    private final PermissionTypeEntityResolver permissionTypeEntityResolver;


    @Override
    public List<PermissionRoleDefinition> getRoles(PermissionType permissionType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PermissionRoleDefinition addRole(PermissionRoleDefinition role,
        PermissionTable permissionTable) {
        Validate.notNull(role);
        Validate.notNull(permissionTable);
        AclRoleEntity roleEntity = buildRoleEntity(role);
        roleEntity = aclRoleRepository.save(roleEntity);
        putEntries(roleEntity, permissionTable);
        role.setId(roleEntity.getId());
        return role;
    }

    private AclRoleEntity buildRoleEntity(PermissionRoleDefinition role) {
        AclPermissionTypeEntity permissionType = permissionTypeEntityResolver
            .resolve(role.getPermissionType());

        Integer targetPosition = aclRoleRepository
            .findTopByPermissionTypeOrderByPositionDesc(permissionType)
            .map(foundRole -> foundRole.getPosition() + 1)
            .orElse(1);

        return AclRoleEntity.builder()
            .name(role.getName())
            .description(role.getDescription())
            .permissionType(permissionType)
            .position(targetPosition)
            .build();
    }

    private void putEntries(AclRoleEntity roleEntity, PermissionTable permissionTable) {
        permissionTable.getPermissions().stream()
            .map(permission -> mapToRoleEntry(permission, roleEntity))
            .forEach(aclRoleEntryRepository::save);
    }

    private AclRoleEntryEntity mapToRoleEntry(Permission permission, AclRoleEntity roleEntity) {
        return AclRoleEntryEntity.builder()
            .role(roleEntity)
            .permission(aclPermissionRepository.findAllByCode(permission.getDefinition().getCode()))
            .entryValue(permission.getValue())
            .build();
    }

    @Override
    public void removeRole(Long roleId) {
        aclRoleRepository.delete(roleId);
    }

    @Override
    public PermissionTable getPermissionTable(PermissionRoleDefinition role) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PermissionRoleDefinition updatePermissionRoleDefinition(PermissionRoleDefinition role) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PermissionTable updatePermissionTable(PermissionRoleDefinition role,
        PermissionTable permissionTable) {
        throw new UnsupportedOperationException();
    }
}
