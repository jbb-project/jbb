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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.api.entry.PermissionValue;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.Permission;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.impl.acl.PermissionTableTranslator;
import org.jbb.permissions.impl.acl.PermissionTranslator;
import org.jbb.permissions.impl.acl.PermissionTypeTranslator;
import org.jbb.permissions.impl.acl.model.AclPermissionEntity;
import org.jbb.permissions.impl.acl.model.AclPermissionTypeEntity;
import org.jbb.permissions.impl.role.dao.AclRoleEntryRepository;
import org.jbb.permissions.impl.role.dao.AclRoleRepository;
import org.jbb.permissions.impl.role.model.AclRoleEntity;
import org.jbb.permissions.impl.role.model.AclRoleEntryEntity;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionRoleServiceImpl implements PermissionRoleService {

    private final PermissionTypeTranslator permissionTypeTranslator;
    private final PermissionTranslator permissionTranslator;
    private final RoleTranslator roleTranslator;
    private final RoleEntryTranslator roleEntryTranslator;
    private final PermissionTableTranslator permissionTableTranslator;

    private final AclRoleRepository aclRoleRepository;
    private final AclRoleEntryRepository aclRoleEntryRepository;


    @Override
    public List<PermissionRoleDefinition> getRoleDefinitions(PermissionType permissionType) {
        AclPermissionTypeEntity permissionTypeEntity = permissionTypeTranslator
            .toEntity(permissionType);
        return aclRoleRepository.findAllByPermissionTypeOrderByPositionAsc(permissionTypeEntity)
            .stream().map(roleTranslator::toApiModel).collect(Collectors.toList());
    }

    @Override
    public PermissionRoleDefinition addRole(PermissionRoleDefinition role,
        PermissionTable permissionTable) {
        AclRoleEntity roleEntity = roleTranslator.toNewEntity(role);
        roleEntity = aclRoleRepository.save(roleEntity);
        putEntries(roleEntity, permissionTable);
        role.setId(roleEntity.getId());
        return role;
    }

    private void putEntries(AclRoleEntity roleEntity, PermissionTable permissionTable) {
        permissionTable.getPermissions().stream()
            .map(permission -> roleEntryTranslator.toEntity(permission, roleEntity))
            .forEach(aclRoleEntryRepository::save);
    }

    @Override
    public void removeRole(Long roleId) {
        aclRoleRepository.delete(roleId);
    }

    @Override
    public PermissionTable getPermissionTable(Long roleId) {
        AclRoleEntity roleEntity = Optional.ofNullable(aclRoleRepository.findOne(roleId))
            .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        List<AclRoleEntryEntity> roleEntries = aclRoleEntryRepository
            .findAllByRole(roleEntity, new Sort("permission.position"));
        return permissionTableTranslator.fromRoleToApiModel(roleEntries);
    }

    @Override
    public PermissionRoleDefinition updateRoleDefinition(PermissionRoleDefinition role) {
        AclRoleEntity roleEntity = roleTranslator.toEntity(role)
            .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        return roleTranslator.toApiModel(aclRoleRepository.save(roleEntity));
    }

    @Override
    public PermissionTable updatePermissionTable(Long roleId,
        PermissionTable permissionTable) {
        AclRoleEntity roleEntity = Optional.ofNullable(aclRoleRepository.findOne(roleId))
            .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        Set<Permission> permissions = permissionTable.getPermissions();
        Set<AclPermissionEntity> permissionEntities = permissions.stream()
            .map(permissionTranslator::toEntity)
            .collect(Collectors.toSet());

        Set<AclRoleEntryEntity> roleEntries = permissionEntities.stream()
            .map(permissionEntity -> aclRoleEntryRepository
                .findByRoleAndPermission(roleEntity, permissionEntity)
                .orElse(AclRoleEntryEntity.builder()
                    .role(roleEntity)
                    .permission(permissionEntity)
                    .build()
                )
            ).collect(Collectors.toSet());

        roleEntries.forEach(entry -> updatePermissionValue(entry, permissions));
        aclRoleEntryRepository.save(roleEntries);
        return getPermissionTable(roleId);
    }

    private void updatePermissionValue(AclRoleEntryEntity entry,
        Set<Permission> permissions) {
        PermissionValue newPermissionValue = permissions.stream()
            .filter(p -> p.getDefinition().getCode().equals(entry.getPermission().getCode()))
            .map(Permission::getValue).findFirst().orElse(PermissionValue.NO);
        entry.setEntryValue(newPermissionValue);
    }


}
