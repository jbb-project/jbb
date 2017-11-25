/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.acl;

import com.google.common.eventbus.Subscribe;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.eventbus.JbbEventBusListener;
import org.jbb.members.event.MemberRegistrationEvent;
import org.jbb.members.event.MemberRemovedEvent;
import org.jbb.permissions.api.identity.MemberIdentity;
import org.jbb.permissions.impl.acl.dao.AclSecurityIdentityRepository;
import org.jbb.permissions.impl.acl.model.AclSecurityIdentityEntity;
import org.jbb.permissions.impl.role.dao.AclActiveRoleRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberEventHandler implements JbbEventBusListener {

    private final SecurityIdentityTranslator securityIdentityTranslator;

    private final AclSecurityIdentityRepository aclSecurityIdentityRepository;
    private final AclActiveRoleRepository aclActiveRoleRepository;

    @Subscribe
    public void addMemberIdentity(MemberRegistrationEvent event) {
        AclSecurityIdentityEntity securityIdentityEntity = securityIdentityTranslator
            .toNewEntity(new MemberIdentity(event.getMemberId()));
        if (securityIdentityEntity.getType() != null) {
            aclSecurityIdentityRepository.save(securityIdentityEntity);
        }
    }

    @Subscribe
    public void removeMemberFromPermission(MemberRemovedEvent event) {
        // remove active roles for this member identity first
        AclSecurityIdentityEntity memberIdentityEntity = securityIdentityTranslator
            .toEntity(new MemberIdentity(event.getMemberId()))
            .orElseThrow(() -> new IllegalStateException("Not found member identity"));
        aclActiveRoleRepository.deleteAllBySecurityIdentity(memberIdentityEntity);

        // remove member identity itself
        aclSecurityIdentityRepository.delete(memberIdentityEntity);
    }

}
