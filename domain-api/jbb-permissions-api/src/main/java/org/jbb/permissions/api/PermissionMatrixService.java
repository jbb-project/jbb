/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.api;


import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.api.matrix.PermissionMatrix;
import org.jbb.permissions.api.permission.PermissionType;

public interface PermissionMatrixService {

    PermissionMatrix getPermissionMatrix(PermissionType permissionType,
        SecurityIdentity securityIdentity);

    void setPermissionMatrix(PermissionMatrix matrix);

}
