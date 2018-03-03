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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/acp/permissions/global-administrators")
public class AcpAdministratorPermissionsController extends AbstractAcpSecurityIdentityChooseController {

    @Override
    public String getPermissionTypeUrlSuffix() {
        return "global-administrators";
    }

    @Override
    public String getViewDescription() {
        return "Administrator permissions";
    }
}
