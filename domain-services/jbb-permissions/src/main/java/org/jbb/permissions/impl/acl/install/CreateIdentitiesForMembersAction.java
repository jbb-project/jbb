/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.acl.install;

import com.github.zafarkhaja.semver.Version;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.install.JbbVersions;
import org.jbb.members.api.base.MemberService;
import org.jbb.permissions.api.identity.MemberIdentity;
import org.jbb.permissions.impl.acl.SecurityIdentityTranslator;
import org.jbb.permissions.impl.acl.dao.AclSecurityIdentityRepository;
import org.jbb.permissions.impl.acl.model.AclSecurityIdentityEntity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Component
@RequiredArgsConstructor
public class CreateIdentitiesForMembersAction implements InstallUpdateAction {

    private final MemberService memberService;
    private final SecurityIdentityTranslator securityIdentityTranslator;
    private final AclSecurityIdentityRepository identityRepository;

    @Override
    public Version fromVersion() {
        return JbbVersions.VERSION_0_10_0;
    }

    @Override
    public void install(InstallationData installationData) {
        List<AclSecurityIdentityEntity> memberIdentities = memberService
                .getAllMembersSortedByRegistrationDate()
                .stream()
                .map(member -> securityIdentityTranslator
                        .toNewEntity(new MemberIdentity(member.getId())))
                .collect(Collectors.toList());

        identityRepository.saveAll(memberIdentities);
    }

}
