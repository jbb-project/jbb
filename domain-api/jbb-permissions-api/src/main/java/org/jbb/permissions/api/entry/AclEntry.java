/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.api.entry;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.api.permission.Permission;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AclEntry {

    @NotNull
    private SecurityIdentity securityIdentity;

    @NotNull
    private Permission permission;

    @NotNull
    private PermissionValue value;


}
