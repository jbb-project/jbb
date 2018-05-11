/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.privilege.health;

import lombok.RequiredArgsConstructor;
import org.jbb.lib.health.JbbHealthCheck;
import org.jbb.security.impl.privilege.dao.AdministratorRepository;
import org.jbb.system.api.install.InstallationService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdministratorExistsHealthCheck extends JbbHealthCheck {

    private final InstallationService installationService;
    private final AdministratorRepository administratorRepository;

    @Override
    public String getName() {
        return "Administrator existence";
    }

    @Override
    protected Result check() throws Exception {
        if (!installationService.isInstalled() || administratorRepository.count() > 0) {
            return Result.healthy();
        } else {
            return Result.unhealthy("No member with administrator privileges found");
        }
    }
}
