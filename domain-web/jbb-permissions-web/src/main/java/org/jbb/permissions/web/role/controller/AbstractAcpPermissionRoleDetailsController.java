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

import com.google.common.collect.Iterables;

import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.web.base.PermissionTableMapper;
import org.jbb.permissions.web.role.PermissionRoleDefinitionMapper;
import org.jbb.permissions.web.role.form.PredefinedRoleForm;
import org.jbb.permissions.web.role.form.RoleDetailsForm;
import org.jbb.permissions.web.role.logic.PredefinedRolesMapper;
import org.jbb.permissions.web.role.model.PredefinedRoleRow;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractAcpPermissionRoleDetailsController {

    private static final String DETAILS_VIEW_NAME = "acp/permissions/role-details";
    private static final String PREDEFINED_CHOOSE_VIEW_NAME = "acp/permissions/predefined-roles-choose";

    private static final String PREDEFINED_ROLE_FORM = "predefinedRoleForm";

    private final PermissionRoleService permissionRoleService;
    private final PermissionRoleDefinitionMapper roleDefinitionMapper;
    private final PermissionTableMapper tableMapper;
    private final PredefinedRolesMapper predefinedRolesMapper;

    public abstract String getPermissionTypeUrlSuffix();

    public abstract PermissionType getPermissionType();

    @RequestMapping(path = "/details", method = RequestMethod.GET)
    public String roleDetailGet(@RequestParam(value = "id") Long roleId, Model model) {
        PermissionRoleDefinition roleDefinition = permissionRoleService.getRoleDefinition(roleId);
        PermissionTable permissionTable = permissionRoleService.getPermissionTable(roleId);

        RoleDetailsForm form = new RoleDetailsForm();
        form.setDefinition(roleDefinitionMapper.toDto(roleDefinition));
        form.setValueMap(tableMapper.toMap(permissionTable));
        model.addAttribute("roleDetailsForm", form);
        model.addAttribute("roleDetails", tableMapper.toDto(permissionTable));
        model.addAttribute("roleTypeSuffix", getPermissionTypeUrlSuffix());

        return DETAILS_VIEW_NAME;
    }

    @RequestMapping(path = "/new", method = RequestMethod.GET)
    public String roleCreate(Model model) {
        List<PermissionRoleDefinition> predefinedRoles = permissionRoleService.getPredefinedRoles(getPermissionType());
        List<PredefinedRoleRow> predefinedRoleRows = predefinedRolesMapper.toRowList(predefinedRoles);
        PredefinedRoleForm form = new PredefinedRoleForm();
        form.setRoleId(Iterables.get(predefinedRoleRows, 0).getRoleId());
        model.addAttribute(PREDEFINED_ROLE_FORM, form);
        model.addAttribute("predefinedRoles", predefinedRoleRows);
        model.addAttribute("roleTypeSuffix", getPermissionTypeUrlSuffix());

        return PREDEFINED_CHOOSE_VIEW_NAME;
    }

    @RequestMapping(path = "/new/details", method = RequestMethod.POST)
    public String newRoleDetails(Model model, @ModelAttribute(PREDEFINED_ROLE_FORM) PredefinedRoleForm form,
                                 BindingResult bindingResult) {
        PermissionTable permissionTable = permissionRoleService.getPermissionTable(form.getRoleId());
        RoleDetailsForm roleForm = new RoleDetailsForm();
        roleForm.setValueMap(tableMapper.toMap(permissionTable));
        model.addAttribute("roleDetailsForm", roleForm);
        model.addAttribute("roleDetails", tableMapper.toDto(permissionTable));
        model.addAttribute("roleTypeSuffix", getPermissionTypeUrlSuffix());

        return DETAILS_VIEW_NAME;
    }

}
