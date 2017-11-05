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

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.permissions.api.PermissionMatrixService;
import org.jbb.permissions.api.entry.PermissionValue;
import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.api.matrix.PermissionMatrix;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.Permission;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.event.PermissionMatrixChangedEvent;
import org.jbb.permissions.impl.PermissionCaches;
import org.jbb.permissions.impl.acl.dao.AclEntryRepository;
import org.jbb.permissions.impl.acl.model.AclEntryEntity;
import org.jbb.permissions.impl.acl.model.AclPermissionCategoryEntity;
import org.jbb.permissions.impl.acl.model.AclPermissionEntity;
import org.jbb.permissions.impl.acl.model.AclPermissionTypeEntity;
import org.jbb.permissions.impl.acl.model.AclSecurityIdentityEntity;
import org.jbb.permissions.impl.role.RoleTranslator;
import org.jbb.permissions.impl.role.dao.AclActiveRoleRepository;
import org.jbb.permissions.impl.role.dao.AclRoleRepository;
import org.jbb.permissions.impl.role.model.AclActiveRoleEntity;
import org.jbb.permissions.impl.role.model.AclRoleEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionMatrixServiceImpl implements PermissionMatrixService {

    private final PermissionTypeTranslator permissionTypeTranslator;
    private final SecurityIdentityTranslator securityIdentityTranslator;
    private final RoleTranslator roleTranslator;
    private final PermissionTableTranslator permissionTableTranslator;
    private final PermissionTranslator permissionTranslator;

    private final AclEntryRepository aclEntryRepository;
    private final AclActiveRoleRepository aclActiveRoleRepository;
    private final AclRoleRepository aclRoleRepository;

    private final PermissionCaches permissionCaches;

    private final JbbEventBus eventBus;

    @Override
    public PermissionMatrix getPermissionMatrix(PermissionType permissionType,
        SecurityIdentity securityIdentity) {
        AclPermissionTypeEntity permissionTypeEntity = permissionTypeTranslator
            .toEntity(permissionType);
        AclSecurityIdentityEntity securityIdentityEntity = securityIdentityTranslator
            .toEntity(securityIdentity)
            .orElseThrow(() -> new IllegalArgumentException("Security identity doesn't exist"));

        Optional<AclActiveRoleEntity> activeRoleEntity = aclActiveRoleRepository
            .findActiveByPermissionTypeAndSecurityIdentity(
                permissionTypeEntity, securityIdentityEntity
            );

        Optional<PermissionRoleDefinition> assignedRole = activeRoleEntity
            .map(activeRole -> roleTranslator.toApiModel(activeRole.getRole()));

        Optional<PermissionTable> permissionTable = Optional.empty();
        if (!activeRoleEntity.isPresent()) {
            List<AclEntryEntity> aclEntries = Lists.newArrayList();
            for (AclPermissionCategoryEntity category : permissionTypeEntity.getCategories()) {
                for (AclPermissionEntity permission : category.getPermissions()) {
                    AclEntryEntity aclEntry = aclEntryRepository
                        .findBySecurityIdentityAndPermission(securityIdentityEntity, permission)
                        .orElse(defaultNegativeEntry(securityIdentityEntity, permission));
                    aclEntries.add(aclEntry);
                }
            }
            permissionTable = Optional.of(permissionTableTranslator.toApiModel(aclEntries));
        }

        return PermissionMatrix.builder()
            .permissionType(permissionType)
            .securityIdentity(securityIdentity)
            .assignedRole(assignedRole)
            .permissionTable(permissionTable)
            .build();

    }

    private AclEntryEntity defaultNegativeEntry(AclSecurityIdentityEntity securityIdentityEntity,
        AclPermissionEntity permission) {
        return AclEntryEntity.builder()
            .securityIdentity(securityIdentityEntity)
            .permission(permission)
            .entryValue(PermissionValue.NO)
            .build();
    }

    @Override
    public void setPermissionMatrix(PermissionMatrix matrix) {
        permissionCaches.clearCaches();
        AclPermissionTypeEntity permissionType = permissionTypeTranslator
            .toEntity(matrix.getPermissionType());
        AclSecurityIdentityEntity securityIdentity = securityIdentityTranslator
            .toEntity(matrix.getSecurityIdentity())
            .orElseThrow(() -> new IllegalArgumentException("Security identity doesn't exist"));

        Optional<PermissionRoleDefinition> assignedRoleOptional = matrix.getAssignedRole();
        Optional<PermissionTable> permissionTableOptional = matrix.getPermissionTable();

        if (assignedRoleOptional.isPresent() && permissionTableOptional.isPresent()) {
            throw new IllegalArgumentException(
                "Matrix can't have assigned role and own permission table");
        } else if (!assignedRoleOptional.isPresent() && !permissionTableOptional.isPresent()) {
            throw new IllegalArgumentException(
                    "Matrix should have assigned role or permission table");
        }

        assignedRoleOptional
            .ifPresent(role -> setRoleForMatrix(role, permissionType, securityIdentity));
        permissionTableOptional.ifPresent(
            table -> setPermissionTableForMatrix(table, permissionType, securityIdentity));

        eventBus.post(new PermissionMatrixChangedEvent(
            matrix.getPermissionType().toString(),
            matrix.getSecurityIdentity().getId(),
            matrix.getSecurityIdentity().getType().toString()
        ));

    }

    private void setRoleForMatrix(PermissionRoleDefinition role,
        AclPermissionTypeEntity permissionType,
        AclSecurityIdentityEntity securityIdentity) {
        // clean up current table
        for (AclPermissionCategoryEntity category : permissionType.getCategories()) {
            for (AclPermissionEntity permission : category.getPermissions()) {
                aclEntryRepository
                    .deleteAllBySecurityIdentityAndPermission(securityIdentity, permission);
            }
        }

        // create or update active role
        AclRoleEntity roleEntity = aclRoleRepository.findOne(role.getId());
        AclActiveRoleEntity aclActiveRole = aclActiveRoleRepository
            .findActiveByPermissionTypeAndSecurityIdentity(permissionType, securityIdentity)
            .orElse(AclActiveRoleEntity.builder().securityIdentity(securityIdentity).build());
        aclActiveRole.setRole(roleEntity);
        aclActiveRoleRepository.save(aclActiveRole);
    }

    private void setPermissionTableForMatrix(PermissionTable permissionTable,
        AclPermissionTypeEntity permissionType,
        AclSecurityIdentityEntity securityIdentity) {
        // clean up active role
        aclActiveRoleRepository.findActiveByPermissionTypeAndSecurityIdentity(
            permissionType, securityIdentity).ifPresent(aclActiveRoleRepository::delete);

        // create or update table
        Set<Permission> permissions = permissionTable.getPermissions();
        Set<AclPermissionEntity> permissionEntities = permissions.stream()
            .map(permissionTranslator::toEntity)
            .collect(Collectors.toSet());

        Set<AclEntryEntity> permissionEntries = permissionEntities.stream()
            .map(permissionEntity -> aclEntryRepository
                .findBySecurityIdentityAndPermission(securityIdentity, permissionEntity)
                .orElse(AclEntryEntity.builder()
                    .securityIdentity(securityIdentity)
                    .permission(permissionEntity)
                    .build()
                )
            ).collect(Collectors.toSet());

        permissionEntries.forEach(entry -> updatePermissionValue(entry, permissions));
        aclEntryRepository.save(permissionEntries);
    }

    private void updatePermissionValue(AclEntryEntity entry, Set<Permission> permissions) {
        PermissionValue newPermissionValue = permissions.stream()
            .filter(p -> p.getDefinition().getCode().equals(entry.getPermission().getCode()))
            .map(Permission::getValue).findFirst().orElse(PermissionValue.NO);
        entry.setEntryValue(newPermissionValue);
    }
}
