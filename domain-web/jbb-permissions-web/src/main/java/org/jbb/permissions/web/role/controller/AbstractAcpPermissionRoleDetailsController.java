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
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.web.base.PermissionTableMapper;
import org.jbb.permissions.web.role.PermissionRoleDefinitionMapper;
import org.jbb.permissions.web.role.form.RoleDetailsForm;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractAcpPermissionRoleDetailsController {

    private static final String VIEW_NAME = "acp/permissions/role-details";

    private final PermissionRoleService permissionRoleService;
    private final PermissionRoleDefinitionMapper roleDefinitionMapper;
    private final PermissionTableMapper tableMapper;

    public abstract String getPermissionTypeUrlSuffix();

    @RequestMapping(path = "/details", method = RequestMethod.GET)
    public String roleDetailGet(@RequestParam(value = "id", required = false) Long roleId, Model model) {
        PermissionRoleDefinition roleDefinition = permissionRoleService.getRoleDefinition(roleId);
        PermissionTable permissionTable = permissionRoleService.getPermissionTable(roleId);

        RoleDetailsForm form = new RoleDetailsForm();
        form.setDefinition(roleDefinitionMapper.toDto(roleDefinition));
        form.setValueMap(tableMapper.toMap(permissionTable));
        model.addAttribute("roleDetailsForm", form);
        model.addAttribute("roleDetails", tableMapper.toDto(permissionTable));
        model.addAttribute("roleTypeSuffix", getPermissionTypeUrlSuffix());

        return VIEW_NAME;
    }

}
