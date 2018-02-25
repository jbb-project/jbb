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

import org.jbb.permissions.web.role.PermissionRolesProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.jbb.permissions.api.permission.PermissionType.MEMBER_PERMISSIONS;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/acp/permissions/role-members")
public class AcpMemberPermissionRolesController {

    private static final String VIEW_NAME = "acp/permissions/role-members";

    private final PermissionRolesProvider rolesProvider;

    @RequestMapping(method = RequestMethod.GET)
    public String permissionsGet(Model model) {
        model.addAttribute("roles", rolesProvider.getRoleDefinition(MEMBER_PERMISSIONS));
        model.addAttribute("hasPermissionToManage", rolesProvider.hasPermissionToManageRoles());
        return VIEW_NAME;
    }

}
