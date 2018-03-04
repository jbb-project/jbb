/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.web.base;

import org.jbb.permissions.api.effective.EffectivePermission;
import org.jbb.permissions.api.effective.EffectivePermissionTable;
import org.jbb.permissions.api.permission.PermissionCategory;
import org.jbb.permissions.web.role.model.EffectivePermissionTableCategory;
import org.jbb.permissions.web.role.model.EffectivePermissionTableRow;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Component
public class EffectivePermissionTableMapper {

    public List<EffectivePermissionTableCategory> toDto(EffectivePermissionTable table) {
        Set<EffectivePermission> permissions = table.getPermissions();
        Map<PermissionCategory, List<EffectivePermission>> categoryMap = permissions.stream()
                .collect(groupingBy(permission -> permission.getDefinition().getCategory()));

        return categoryMap.keySet().stream()
                .map(category -> mapToDto(category, categoryMap.get(category)))
                .sorted(Comparator.comparing(EffectivePermissionTableCategory::getPosition))
                .collect(Collectors.toList());
    }

    private EffectivePermissionTableCategory mapToDto(PermissionCategory category, List<EffectivePermission> permissions) {
        return EffectivePermissionTableCategory.builder()
                .name(category.getName())
                .position(category.getPosition())
                .rows(mapToRows(permissions))
                .build();

    }

    private List<EffectivePermissionTableRow> mapToRows(List<EffectivePermission> permissions) {
        return permissions.stream()
                .map(this::mapToRow)
                .sorted(Comparator.comparing(EffectivePermissionTableRow::getPosition))
                .collect(Collectors.toList());
    }

    private EffectivePermissionTableRow mapToRow(EffectivePermission permission) {
        return EffectivePermissionTableRow.builder()
                .name(permission.getDefinition().getName())
                .code(permission.getDefinition().getCode())
                .position(permission.getDefinition().getPosition())
                .verdict(permission.getVerdict())
                .build();
    }

}
