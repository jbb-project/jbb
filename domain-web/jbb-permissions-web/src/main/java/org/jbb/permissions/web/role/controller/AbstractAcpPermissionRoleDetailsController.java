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
import org.jbb.permissions.api.exceptions.RemovePredefinedRoleException;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.api.role.PredefinedRole;
import org.jbb.permissions.web.base.PermissionTableMapper;
import org.jbb.permissions.web.role.PermissionRoleDefinitionMapper;
import org.jbb.permissions.web.role.RoleDefinition;
import org.jbb.permissions.web.role.form.DeleteRoleForm;
import org.jbb.permissions.web.role.form.MoveRoleForm;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractAcpPermissionRoleDetailsController {

    private static final String DETAILS_VIEW_NAME = "acp/permissions/role-details";
    private static final String PREDEFINED_CHOOSE_VIEW_NAME = "acp/permissions/predefined-roles-choose";

    private static final String PREDEFINED_ROLE_FORM = "predefinedRoleForm";
    private static final String ROLE_DETAILS_FORM = "roleDetailsForm";
    private static final String DELETE_ROLE_FORM = "deleteRoleForm";
    private static final String MOVE_ROLE_FORM = "moveRoleForm";

    private static final String ROLE_DETAILS = "roleDetails";
    private static final String ROLE_TYPE_SUFFIX = "roleTypeSuffix";

    private static final String REDIRECT = "redirect:/acp/permissions/";

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
        model.addAttribute(ROLE_DETAILS_FORM, form);
        model.addAttribute(ROLE_DETAILS, tableMapper.toDto(permissionTable));
        model.addAttribute(ROLE_TYPE_SUFFIX, getPermissionTypeUrlSuffix());

        return DETAILS_VIEW_NAME;
    }

    @RequestMapping(path = "/new", method = RequestMethod.GET)
    public String roleCreate(Model model) {
        List<PermissionRoleDefinition> predefinedRoles = permissionRoleService.getPredefinedRoles(getPermissionType());
        List<PredefinedRoleRow> predefinedRoleRows = predefinedRolesMapper.toRowList(predefinedRoles);
        PredefinedRoleForm form = new PredefinedRoleForm();
        form.setRoleId(Iterables.get(predefinedRoleRows, 0).getRoleId());
        model.addAttribute(PREDEFINED_ROLE_FORM, form);
        model.addAttribute(ROLE_TYPE_SUFFIX, getPermissionTypeUrlSuffix());
        model.addAttribute("predefinedRoles", predefinedRoleRows);

        return PREDEFINED_CHOOSE_VIEW_NAME;
    }

    @RequestMapping(path = "/new/details", method = RequestMethod.POST)
    public String newRoleDetails(Model model, @ModelAttribute(PREDEFINED_ROLE_FORM) PredefinedRoleForm form) {
        PermissionRoleDefinition predefinedRoleDef = permissionRoleService.getRoleDefinition(form.getRoleId());
        PermissionTable permissionTable = permissionRoleService.getPermissionTable(form.getRoleId());
        RoleDetailsForm roleForm = new RoleDetailsForm();
        roleForm.setValueMap(tableMapper.toMap(permissionTable));
        roleForm.setDefinition(new RoleDefinition());
        Optional<PredefinedRole> predefinedRole = predefinedRoleDef.getPredefinedRole();
        if (predefinedRole.isPresent()) {
            roleForm.getDefinition().setSourcePredefinedRole(predefinedRole.get());
        } else {
            roleForm.getDefinition().setSourcePredefinedRole(predefinedRoleDef.getSourcePredefinedRole());
        }
        model.addAttribute(ROLE_DETAILS_FORM, roleForm);
        model.addAttribute(ROLE_DETAILS, tableMapper.toDto(permissionTable));
        model.addAttribute(ROLE_TYPE_SUFFIX, getPermissionTypeUrlSuffix());

        return DETAILS_VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createOrUpdateRole(Model model, @ModelAttribute(ROLE_DETAILS_FORM) @Valid RoleDetailsForm form,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ROLE_DETAILS, tableMapper.toDto(tableMapper.toModel(form.getValueMap())));
            model.addAttribute(ROLE_TYPE_SUFFIX, getPermissionTypeUrlSuffix());
            return DETAILS_VIEW_NAME;
        }
        form.getDefinition().setPermissionType(getPermissionType());
        Long id = form.getDefinition().getId();
        if (id == null) {
            permissionRoleService.addRole(
                    roleDefinitionMapper.toModel(form.getDefinition()),
                    tableMapper.toModel(form.getValueMap())
            );
        } else {
            permissionRoleService.updateRoleDefinition(roleDefinitionMapper.toModel(form.getDefinition()));
            permissionRoleService.updatePermissionTable(id, tableMapper.toModel(form.getValueMap()));
        }

        return "redirect:" + getPermissionTypeUrlSuffix();
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public String deleteRole(@ModelAttribute(DELETE_ROLE_FORM) DeleteRoleForm form,
                             RedirectAttributes redirectAttributes) {
        try {
            permissionRoleService.removeRole(form.getId());
        } catch (RemovePredefinedRoleException e) {
            redirectAttributes.addFlashAttribute("removePredefinedRoleError", true);
        }
        return REDIRECT + getPermissionTypeUrlSuffix();
    }

    @RequestMapping(path = "/moveup", method = RequestMethod.POST)
    public String moveUpRole(@ModelAttribute(MOVE_ROLE_FORM) MoveRoleForm form) {
        permissionRoleService.moveRoleToPosition(form.getId(), form.getPosition() - 1);
        return REDIRECT + getPermissionTypeUrlSuffix();
    }

    @RequestMapping(path = "/movedown", method = RequestMethod.POST)
    public String moveDownRole(@ModelAttribute(MOVE_ROLE_FORM) MoveRoleForm form) {
        permissionRoleService.moveRoleToPosition(form.getId(), form.getPosition() + 1);
        return REDIRECT + getPermissionTypeUrlSuffix();
    }

}
