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
import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.api.matrix.PermissionMatrix;
import org.jbb.permissions.api.matrix.PermissionTable;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.role.PermissionRoleDefinition;
import org.jbb.permissions.web.base.PermissionTableMapper;
import org.jbb.permissions.web.base.SecurityIdentityMapper;
import org.jbb.permissions.web.base.form.PermissionMatrixForm;
import org.jbb.permissions.web.base.form.SecurityIdentityChooseForm;
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

    private final PermissionMatrixService permissionMatrixService;
    private final PermissionRoleService permissionRoleService;

    public abstract String getViewName();

    public abstract PermissionType getPermissionType();

    @RequestMapping(method = RequestMethod.POST)
    public String showAdministratorPermissions(Model model,
                                               @ModelAttribute(SECURITY_IDENTITY_FORM) SecurityIdentityChooseForm form,
                                               BindingResult bindingResult) {
        PermissionMatrixForm matrixForm = new PermissionMatrixForm();
        Optional<SecurityIdentity> securityIdentity = securityIdentityMapper.toModel(form);
        if (securityIdentity.isPresent()) {
            model.addAttribute("roles", rolesMapper.toRowList(permissionRoleService.getRoleDefinitions(getPermissionType())));
            PermissionMatrix permissionMatrix = permissionMatrixService.getPermissionMatrix(getPermissionType(), securityIdentity.get());
            Optional<PermissionRoleDefinition> assignedRole = permissionMatrix.getAssignedRole();
            Optional<PermissionTable> permissionTable = permissionMatrix.getPermissionTable();
            PermissionTable usedPermissionTable = null;
            if (assignedRole.isPresent()) {
                usedPermissionTable = permissionRoleService.getPermissionTable(assignedRole.get().getId());
                matrixForm.setMatrixMode(MatrixMode.ASSIGNED_ROLE);
                matrixForm.setRoleId(assignedRole.get().getId());
                matrixForm.setValueMap(tableMapper.toMap(usedPermissionTable));
            } else if (permissionTable.isPresent()) {
                usedPermissionTable = permissionTable.get();
                matrixForm.setMatrixMode(MatrixMode.PERMISSION_TABLE);
                matrixForm.setValueMap(tableMapper.toMap(usedPermissionTable));
            } else {
                throw new IllegalStateException("Invalid permission matrix");
            }
            model.addAttribute("permissionTable", tableMapper.toDto(usedPermissionTable));
            model.addAttribute(PERMISSION_MATRIX_FORM, matrixForm);
            return getViewName();
        } else {
            bindingResult.rejectValue("memberDisplayedName", "x", "Member not found");
            return fillSecurityIdentityAttributes(model);
        }
    }
}
