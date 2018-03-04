/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.web.base.controller;

import org.jbb.permissions.api.PermissionMatrixService;
import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.web.base.logic.PermissionMatrixMapper;
import org.jbb.permissions.web.base.logic.PermissionTableMapper;
import org.jbb.permissions.web.base.logic.SecurityIdentityMapper;
import org.jbb.permissions.web.role.logic.RolesMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/acp/permissions/global-administrators")
public class AcpAdministratorPermissionsController extends AbstractAcpPermissionsController {

    private static final String VIEW_NAME = "acp/permissions/global-administrators";

    public AcpAdministratorPermissionsController(SecurityIdentityMapper securityIdentityMapper,
                                                 PermissionTableMapper tableMapper,
                                                 RolesMapper rolesMapper,
                                                 PermissionMatrixMapper matrixMapper,
                                                 PermissionMatrixService permissionMatrixService,
                                                 PermissionRoleService permissionRoleService) {
        super(securityIdentityMapper, tableMapper, rolesMapper, matrixMapper, permissionMatrixService, permissionRoleService);
    }

    @Override
    public String getPermissionTypeUrlSuffix() {
        return "global-administrators";
    }

    @Override
    public String getViewDescription() {
        return "Administrator permissions";
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }

    @Override
    public PermissionType getPermissionType() {
        return PermissionType.ADMINISTRATOR_PERMISSIONS;
    }
}
