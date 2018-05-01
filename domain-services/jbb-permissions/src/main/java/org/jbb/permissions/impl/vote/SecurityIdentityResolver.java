/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.vote;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import org.jbb.permissions.api.identity.AdministratorGroupIdentity;
import org.jbb.permissions.api.identity.AnonymousIdentity;
import org.jbb.permissions.api.identity.MemberIdentity;
import org.jbb.permissions.api.identity.RegisteredMembersIdentity;
import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.api.identity.SecurityIdentity.Type;
import org.jbb.security.api.role.RoleService;
import org.springframework.stereotype.Component;

import java.util.Set;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityIdentityResolver {

    private final RoleService roleService;

    public Set<SecurityIdentity> resolveAffectedIdentities(SecurityIdentity securityIdentity) {
        if (securityIdentity.getType() == Type.ANONYMOUS ||
                (securityIdentity.getType() == Type.MEMBER && securityIdentity.getId() == 0)) {
            return Sets.newHashSet(AnonymousIdentity.getInstance());
        }

        if (securityIdentity.getType() == Type.REGISTERED_MEMBERS) {
            return Sets.newHashSet(RegisteredMembersIdentity.getInstance());
        }

        if (securityIdentity.getType() == Type.ADMIN_GROUP) {
            return Sets.newHashSet(RegisteredMembersIdentity.getInstance(),
                    AdministratorGroupIdentity.getInstance());
        }

        // MEMBER type processing
        Long memberId = securityIdentity.getId();
        Set<SecurityIdentity> memberIdentities = Sets
                .newHashSet(RegisteredMembersIdentity.getInstance());
        if (roleService.hasAdministratorRole(memberId)) {
            memberIdentities.add(AdministratorGroupIdentity.getInstance());
        }
        memberIdentities.add(new MemberIdentity(memberId));

        return ImmutableSet.copyOf(memberIdentities);


    }
}
