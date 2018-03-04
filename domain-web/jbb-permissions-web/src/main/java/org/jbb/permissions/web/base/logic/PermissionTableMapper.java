/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.web.base.logic;

import org.jbb.permissions.api.PermissionService;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.Permission;
import org.jbb.permissions.api.permission.PermissionCategory;
import org.jbb.permissions.api.permission.PermissionDefinition;
import org.jbb.permissions.api.permission.PermissionValue;
import org.jbb.permissions.web.role.model.PermissionTableCategory;
import org.jbb.permissions.web.role.model.PermissionTableRow;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import static java.util.stream.Collectors.groupingBy;

@Component
@RequiredArgsConstructor
public class PermissionTableMapper {

    private final PermissionService permissionService;

    public List<PermissionTableCategory> toDto(PermissionTable table) {
        Set<Permission> permissions = table.getPermissions();
        Map<PermissionCategory, List<Permission>> categoryMap = permissions.stream()
                .collect(groupingBy(permission -> permission.getDefinition().getCategory()));

        return categoryMap.keySet().stream()
                .map(category -> mapToDto(category, categoryMap.get(category)))
                .sorted(Comparator.comparing(PermissionTableCategory::getPosition))
                .collect(Collectors.toList());
    }

    private PermissionTableCategory mapToDto(PermissionCategory category, List<Permission> permissions) {
        return PermissionTableCategory.builder()
                .name(category.getName())
                .position(category.getPosition())
                .rows(mapToRows(permissions))
                .build();

    }

    private List<PermissionTableRow> mapToRows(List<Permission> permissions) {
        return permissions.stream()
                .map(this::mapToRow)
                .sorted(Comparator.comparing(PermissionTableRow::getPosition))
                .collect(Collectors.toList());
    }

    private PermissionTableRow mapToRow(Permission permission) {
        return PermissionTableRow.builder()
                .name(permission.getDefinition().getName())
                .code(permission.getDefinition().getCode())
                .position(permission.getDefinition().getPosition())
                .value(permission.getValue())
                .build();
    }

    public Map<String, PermissionValue> toMap(PermissionTable permissionTable) {
        return permissionTable.getPermissions().stream()
                .collect(Collectors.groupingBy(permission -> permission.getDefinition().getCode(),
                        Collectors.collectingAndThen(Collectors.toList(), list -> list.get(0).getValue())));
    }

    public PermissionTable toModel(Map<String, PermissionValue> valueMap) {
        PermissionTable.Builder builder = PermissionTable.builder();
        valueMap.keySet().stream()
                .map(code -> getPermission(code, valueMap))
                .forEach(builder::putPermission);
        return builder.build();
    }

    private Permission getPermission(String code, Map<String, PermissionValue> valueMap) {
        PermissionDefinition definition = permissionService.getPermissionDefinitionByCode(code)
                .orElseThrow(IllegalArgumentException::new);
        return new Permission(definition, valueMap.get(code));

    }
}
