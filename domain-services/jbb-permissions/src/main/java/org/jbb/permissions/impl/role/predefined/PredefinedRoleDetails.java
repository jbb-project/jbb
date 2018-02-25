/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.role.predefined;

import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.api.role.PredefinedRole;

public interface PredefinedRoleDetails {

    PredefinedRole getPredefinedRole();

    PermissionRoleDefinition getDefinition();

    PermissionTable getPermissionTable();

}
