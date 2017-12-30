/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base;

import com.github.zafarkhaja.semver.Version;

import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.install.JbbVersions;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.IPAddress;
import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.registration.RegistrationRequest;
import org.jbb.members.api.registration.RegistrationService;
import org.jbb.security.api.role.RoleService;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInstallAction implements InstallUpdateAction {

    private final RegistrationService registrationService;
    private final MemberService memberService;
    private final RoleService roleService;

    @Override
    public Version fromVersion() {
        return JbbVersions.VERSION_0_4_0;
    }

    @Override
    public void install(InstallationData installationData) {
        RegistrationRequest registrationRequest = buildRequest(installationData);
        registrationService.register(registrationRequest);
        Optional<Member> adminMember = memberService
                .getMemberWithUsername(registrationRequest.getUsername());
        adminMember.ifPresent(
                member -> roleService.addAdministratorRole(member.getId())
        );
    }

    private RegistrationRequest buildRequest(InstallationData installationData) {
        return RegistrationRequest.builder()
                .username(Username.builder().value(installationData.getAdminUsername()).build())
                .displayedName(
                        DisplayedName.builder().value(installationData.getAdminDisplayedName()).build())
                .email(Email.builder().value(installationData.getAdminEmail()).build())
                .password(
                        Password.builder().value(installationData.getAdminPassword().toCharArray()).build())
                .passwordAgain(
                        Password.builder().value(installationData.getAdminPassword().toCharArray()).build())
                .ipAddress(getIpAddress())
                .build();
    }

    private IPAddress getIpAddress() {
        try {
            return IPAddress.builder().value(Inet4Address.getLocalHost().getHostAddress()).build();
        } catch (UnknownHostException e) {
            throw new IllegalStateException(e);
        }
    }
}
