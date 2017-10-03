/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.vote;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jbb.permissions.api.PermissionMatrixService;
import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.api.effective.EffectivePermission;
import org.jbb.permissions.api.effective.PermissionVerdict;
import org.jbb.permissions.api.entry.PermissionValue;
import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.api.matrix.PermissionMatrix;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.Permission;
import org.jbb.permissions.api.permission.PermissionDefinition;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EffectivePermissionsBuilder {

    private final PermissionMatrixService permissionMatrixService;
    private final PermissionRoleService permissionRoleService;

    public Set<EffectivePermission> mergePermissions(PermissionType permissionType,
        Set<SecurityIdentity> affectedSecurityIdentities) {
        Set<PermissionMatrix> permissionMatrices = affectedSecurityIdentities.stream()
            .map(securityIdentity -> permissionMatrixService
                .getPermissionMatrix(permissionType, securityIdentity))
            .collect(Collectors.toSet());

        Set<PermissionTable> permissionTables = permissionMatrices.stream()
            .map(this::extractPermissionTable).collect(Collectors.toSet());

        Set<Permission> permissions = permissionTables.stream()
            .flatMap(permissionTable -> permissionTable.getPermissions().stream())
            .collect(Collectors.toSet());

        Map<PermissionDefinition, Set<PermissionValue>> permissionValuesMap = permissions.stream()
            .collect(Collectors.groupingBy(Permission::getDefinition, Collectors.mapping(
                Permission::getValue, Collectors.toSet())));

        return permissionValuesMap.keySet().stream()
            .map(definition -> countEffectivePermission(definition,
                permissionValuesMap.get(definition)))
            .collect(Collectors.toSet());

    }

    private EffectivePermission countEffectivePermission(PermissionDefinition definition,
        Set<PermissionValue> permissionValues) {
        return EffectivePermission.builder()
            .definition(definition)
            .verdict(countEffectiveVerdict(permissionValues))
            .build();
    }

    private PermissionVerdict countEffectiveVerdict(Set<PermissionValue> permissionValues) {
        if (permissionValues.isEmpty() || permissionValues.contains(PermissionValue.NEVER)
            || !permissionValues.contains(PermissionValue.YES)) {
            return PermissionVerdict.FORBIDDEN;
        }
        return PermissionVerdict.ALLOW;
    }

    private PermissionTable extractPermissionTable(PermissionMatrix matrix) {
        Optional<PermissionRoleDefinition> role = matrix.getAssignedRole();
        Optional<PermissionTable> permissionTable = matrix.getPermissionTable();
        if (role.isPresent()) {
            return permissionRoleService.getPermissionTable(role.get().getId());
        } else if (permissionTable.isPresent()) {
            return permissionTable.get();
        }
        throw new IllegalStateException("Matrix hasn't role nor permission table");
    }

}
