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
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.web.base.PermissionTableMapper;
import org.jbb.permissions.web.role.PermissionRoleDefinitionMapper;
import org.jbb.permissions.web.role.logic.PredefinedRolesMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/acp/permissions/role-members")
public class AcpMemberPermissionRoleDetailsController extends AbstractAcpPermissionRoleDetailsController {

    public AcpMemberPermissionRoleDetailsController(PermissionRoleService permissionRoleService,
                                                    PermissionRoleDefinitionMapper roleDefinitionMapper,
                                                    PermissionTableMapper tableMapper,
                                                    PredefinedRolesMapper predefinedRolesMapper) {
        super(permissionRoleService, roleDefinitionMapper, tableMapper, predefinedRolesMapper);
    }

    @Override
    public String getPermissionTypeUrlSuffix() {
        return "role-members";
    }

    @Override
    public PermissionType getPermissionType() {
        return PermissionType.MEMBER_PERMISSIONS;
    }
}
