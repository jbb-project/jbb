/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.role.install;

import com.github.zafarkhaja.semver.Version;

import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.install.JbbVersions;
import org.jbb.permissions.api.PermissionRoleService;
import org.jbb.permissions.impl.role.install.predefined.PredefinedRoleDetails;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Order(2)
@Component
@RequiredArgsConstructor
public class AclRoleInstallAction implements InstallUpdateAction {

    private final PermissionRoleService permissionRoleService;

    private final List<PredefinedRoleDetails> predefinedRolesDetails;

    @Override
    public Version fromVersion() {
        return JbbVersions.VERSION_0_10_0;
    }

    @Override
    public void install(InstallationData installationData) {
        predefinedRolesDetails.forEach(
                role -> permissionRoleService.addRole(role.getDefinition(), role.getPermissionTable())
        );
    }
}
