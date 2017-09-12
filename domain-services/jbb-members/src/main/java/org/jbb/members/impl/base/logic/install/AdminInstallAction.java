/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.logic.install;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jbb.install.InstallAction;
import org.jbb.install.InstallationData;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.registration.RegistrationService;
import org.jbb.security.api.role.RoleService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInstallAction implements InstallAction {

    private final RegistrationService registrationService;
    private final MemberService memberService;
    private final RoleService roleService;

    @Override
    public void install(InstallationData installationData) {
        AdministratorRegistrationRequest registrationRequest = new AdministratorRegistrationRequest(
            installationData);
        registrationService.register(registrationRequest);
        Optional<Member> adminMember = memberService
            .getMemberWithUsername(registrationRequest.getUsername());
        adminMember.ifPresent(
            member -> roleService.addAdministratorRole(member.getId())
        );
    }
}
