/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.web.effective;

import org.jbb.permissions.api.PermissionService;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.web.base.EffectivePermissionTableMapper;
import org.jbb.permissions.web.base.SecurityIdentityMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/acp/permissions/effective-members")
public class AcpEffectiveMemberPermissionsController extends AbstractAcpEffectivePermissionsController {

    private static final String VIEW_NAME = "acp/permissions/effective-members";


    public AcpEffectiveMemberPermissionsController(PermissionService permissionService,
                                                   SecurityIdentityMapper securityIdentityMapper,
                                                   EffectivePermissionTableMapper tableMapper) {
        super(permissionService, securityIdentityMapper, tableMapper);
    }

    @Override
    public String getPermissionTypeUrlSuffix() {
        return "effective-members";
    }

    @Override
    public String getViewDescription() {
        return "Effective member permissions";
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }

    @Override
    public PermissionType getPermissionType() {
        return PermissionType.MEMBER_PERMISSIONS;
    }
}
