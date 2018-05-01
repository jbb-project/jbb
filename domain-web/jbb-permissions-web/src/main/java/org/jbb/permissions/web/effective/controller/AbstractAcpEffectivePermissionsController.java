/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.web.effective.controller;

import org.jbb.permissions.api.PermissionService;
import org.jbb.permissions.api.effective.EffectivePermissionTable;
import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.web.base.controller.AbstractAcpSecurityIdentityChooseController;
import org.jbb.permissions.web.base.form.SecurityIdentityChooseForm;
import org.jbb.permissions.web.base.logic.SecurityIdentityMapper;
import org.jbb.permissions.web.effective.logic.EffectivePermissionTableMapper;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractAcpEffectivePermissionsController extends AbstractAcpSecurityIdentityChooseController {

    private final PermissionService permissionService;
    private final SecurityIdentityMapper securityIdentityMapper;
    private final EffectivePermissionTableMapper tableMapper;

    public abstract String getViewName();

    public abstract PermissionType getPermissionType();

    @RequestMapping(method = RequestMethod.POST)
    public String showAdministratorPermissions(Model model,
                                               @ModelAttribute(SECURITY_IDENTITY_FORM) SecurityIdentityChooseForm form,
                                               BindingResult bindingResult) {
        Optional<SecurityIdentity> securityIdentity = securityIdentityMapper.toModel(form);
        if (securityIdentity.isPresent()) {
            EffectivePermissionTable effectivePermissionTable = permissionService.getEffectivePermissionTable(
                    getPermissionType(), securityIdentity.get());
            model.addAttribute("effectivePermissions", tableMapper.toDto(effectivePermissionTable));
            return getViewName();
        } else {
            bindingResult.rejectValue("memberDisplayedName", "x", "Member not found");
            return fillSecurityIdentityAttributes(model);
        }
    }
}
