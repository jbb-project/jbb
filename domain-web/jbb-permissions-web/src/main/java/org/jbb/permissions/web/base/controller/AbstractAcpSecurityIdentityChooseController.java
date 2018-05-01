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

import org.jbb.permissions.web.base.form.SecurityIdentityChooseForm;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractAcpSecurityIdentityChooseController {
    private static final String VIEW_NAME = "acp/permissions/security-identity-choose";

    protected static final String SECURITY_IDENTITY_FORM = "securityIdentityForm";

    public abstract String getPermissionTypeUrlSuffix();

    public abstract String getViewDescription();

    @RequestMapping(method = RequestMethod.GET)
    public String securityIdentityGet(Model model) {
        model.addAttribute(SECURITY_IDENTITY_FORM, new SecurityIdentityChooseForm());
        return fillSecurityIdentityAttributes(model);
    }

    protected String fillSecurityIdentityAttributes(Model model) {
        model.addAttribute("roleTypeSuffix", getPermissionTypeUrlSuffix());
        model.addAttribute("permissionViewName", getViewDescription());
        return VIEW_NAME;
    }
}
