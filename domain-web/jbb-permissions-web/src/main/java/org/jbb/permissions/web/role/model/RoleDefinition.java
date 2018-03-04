/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.web.role.model;

import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PredefinedRole;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDefinition {

    private Long id;

    @Size(min = 1, max = 255)
    private String name;

    @Size(max = 255)
    private String description;

    private Integer position;

    private PermissionType permissionType;

    private PredefinedRole sourcePredefinedRole;

}
