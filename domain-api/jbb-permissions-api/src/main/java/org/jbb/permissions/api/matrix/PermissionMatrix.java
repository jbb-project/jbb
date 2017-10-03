/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.api.matrix;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionMatrix {

    private PermissionType permissionType;

    private SecurityIdentity securityIdentity;

    @Builder.Default
    private Optional<PermissionRoleDefinition> assignedRole = Optional.empty();

    @Builder.Default
    private Optional<PermissionTable> permissionTable = Optional.empty();


}
