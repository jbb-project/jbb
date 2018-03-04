/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.web.role.logic;

import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.web.role.model.RoleDefinition;
import org.springframework.stereotype.Component;

@Component
public class PermissionRoleDefinitionMapper {

    public RoleDefinition toDto(PermissionRoleDefinition roleDefinition) {
        return RoleDefinition.builder()
                .id(roleDefinition.getId())
                .name(roleDefinition.getName())
                .description(roleDefinition.getDescription())
                .position(roleDefinition.getPosition())
                .sourcePredefinedRole(roleDefinition.getSourcePredefinedRole())
                .permissionType(roleDefinition.getPermissionType())
                .build();
    }

    public PermissionRoleDefinition toModel(RoleDefinition definition) {
        return PermissionRoleDefinition.builder()
                .id(definition.getId())
                .name(definition.getName())
                .description(definition.getDescription())
                .sourcePredefinedRole(definition.getSourcePredefinedRole())
                .position(definition.getPosition())
                .permissionType(definition.getPermissionType())
                .build();
    }
}
