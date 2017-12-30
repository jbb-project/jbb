/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.web.role;

import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.springframework.stereotype.Component;

@Component
public class PermissionRoleDefinitionMapper {

    public RoleDefinition toDto(PermissionRoleDefinition roleDefinition) {
        return RoleDefinition.builder()
                .id(roleDefinition.getId())
                .name(roleDefinition.getName())
                .description(roleDefinition.getDescription())
                .position(roleDefinition.getPosition())
                .build();
    }
}
