/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.api.role;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.jbb.permissions.api.permission.PermissionType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionRoleDefinition {

    @NotBlank
    private String name;

    @NotNull
    private String description;

    @NotNull
    private PermissionType permissionType;

    @Min(0)
    private Integer position;

}
