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
import org.jbb.permissions.api.PermissionService;
import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.api.matrix.PermissionMatrix;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.permission.domain.AdministratorPermissions;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.web.base.form.PermissionMatrixForm;
import org.jbb.permissions.web.base.form.SecurityIdentityChooseForm;
import org.jbb.permissions.web.base.logic.PermissionMatrixMapper;
import org.jbb.permissions.web.base.logic.PermissionTableMapper;
import org.jbb.permissions.web.base.logic.SecurityIdentityMapper;
import org.jbb.permissions.web.base.model.MatrixMode;
import org.jbb.permissions.web.role.logic.RolesMapper;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractAcpPermissionsController extends AbstractAcpSecurityIdentityChooseController {

    private static final String PERMISSION_MATRIX_FORM = "permissionMatrixForm";

    private final SecurityIdentityMapper securityIdentityMapper;
    private final PermissionTableMapper tableMapper;
    private final RolesMapper rolesMapper;
    private final PermissionMatrixMapper matrixMapper;

    private final PermissionMatrixService permissionMatrixService;
    private final PermissionRoleService permissionRoleService;
    private final PermissionService permissionService;

    public abstract String getViewName();

    public abstract PermissionType getPermissionType();

    public abstract AdministratorPermissions permissionToEdit();

    @RequestMapping(method = RequestMethod.POST)
    public String showPermissions(Model model,
                                  @ModelAttribute(SECURITY_IDENTITY_FORM) SecurityIdentityChooseForm form,
                                  BindingResult bindingResult) {
        PermissionMatrixForm matrixForm = new PermissionMatrixForm();
        Optional<SecurityIdentity> securityIdentity = securityIdentityMapper.toModel(form);
        if (securityIdentity.isPresent()) {
            model.addAttribute("roles", rolesMapper.toRowList(permissionRoleService.getRoleDefinitions(getPermissionType())));
            PermissionMatrix permissionMatrix = permissionMatrixService.getPermissionMatrix(getPermissionType(), securityIdentity.get());
            Optional<PermissionRoleDefinition> assignedRole = permissionMatrix.getAssignedRole();
            Optional<PermissionTable> permissionTable = permissionMatrix.getPermissionTable();
            PermissionTable usedPermissionTable = matrixMapper.getUsedPermissionTable(permissionMatrix);
            if (assignedRole.isPresent()) {
                matrixForm.setMatrixMode(MatrixMode.ASSIGNED_ROLE);
                matrixForm.setRoleId(assignedRole.get().getId());
            } else if (permissionTable.isPresent()) {
                matrixForm.setMatrixMode(MatrixMode.PERMISSION_TABLE);
            } else {
                throw new IllegalStateException("Invalid permission matrix");
            }
            matrixForm.setValueMap(tableMapper.toMap(usedPermissionTable));
            model.addAttribute("permissionTable", tableMapper.toDto(usedPermissionTable));
            matrixForm.setSecurityIdentity(form);
            model.addAttribute(PERMISSION_MATRIX_FORM, matrixForm);
            model.addAttribute("securityIdentity", form);
            model.addAttribute("roleTypeSuffix", getPermissionTypeUrlSuffix());
            return getViewName();
        } else {
            bindingResult.rejectValue("memberDisplayedName", "x", "Member not found");
            return fillSecurityIdentityAttributes(model);
        }
    }

    @RequestMapping(path = "/matrix", method = RequestMethod.POST)
    public String updateMatrix(Model model, @ModelAttribute(PERMISSION_MATRIX_FORM) PermissionMatrixForm form) {

        permissionService.assertPermission(permissionToEdit());

        PermissionMatrix matrix = matrixMapper.toModel(form, getPermissionType());
        permissionMatrixService.setPermissionMatrix(matrix);

        PermissionTable usedPermissionTable = matrixMapper.getUsedPermissionTable(
                permissionMatrixService.getPermissionMatrix(
                        getPermissionType(),
                        securityIdentityMapper.toModel(form.getSecurityIdentity())
                                .orElseThrow(IllegalStateException::new)
                )
        );

        model.addAttribute("permissionTable", tableMapper.toDto(usedPermissionTable));
        model.addAttribute("securityIdentity", form.getSecurityIdentity());
        model.addAttribute("roles", rolesMapper.toRowList(permissionRoleService.getRoleDefinitions(getPermissionType())));
        model.addAttribute("roleTypeSuffix", getPermissionTypeUrlSuffix());
        model.addAttribute("permissionMatrixFormSaved", true);
        form.setValueMap(tableMapper.toMap(usedPermissionTable));

        return getViewName();
    }
}
