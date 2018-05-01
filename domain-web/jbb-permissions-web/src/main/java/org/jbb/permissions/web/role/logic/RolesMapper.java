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
import org.jbb.permissions.web.role.model.RoleRow;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RolesMapper {

    public List<RoleRow> toRowList(List<PermissionRoleDefinition> predefinedRoles) {
        return predefinedRoles.stream()
                .map(role -> new RoleRow(role.getId(), role.getName()))
                .collect(Collectors.toList());
    }
}
