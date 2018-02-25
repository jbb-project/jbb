/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.web.role.controller;

import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.web.base.PermissionTableMapper;
import org.jbb.permissions.web.role.PermissionRoleDefinitionMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/acp/permissions/role-administrators")
public class AcpAdministratorPermissionRoleDetailsController extends AbstractAcpPermissionRoleDetailsController {


    public AcpAdministratorPermissionRoleDetailsController(PermissionRoleService permissionRoleService,
                                                           PermissionRoleDefinitionMapper roleDefinitionMapper,
                                                           PermissionTableMapper tableMapper) {
        super(permissionRoleService, roleDefinitionMapper, tableMapper);
    }

    @Override
    public String getPermissionTypeUrlSuffix() {
        return "role-administrators";
    }
}
