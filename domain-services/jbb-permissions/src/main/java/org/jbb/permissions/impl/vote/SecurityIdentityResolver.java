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
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.jbb.permissions.api.identity.AdministratorGroupIdentity;
import org.jbb.permissions.api.identity.AnonymousIdentity;
import org.jbb.permissions.api.identity.MemberIdentity;
import org.jbb.permissions.api.identity.RegisteredMembersIdentity;
import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.security.api.role.RoleService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityIdentityResolver {

    private final RoleService roleService;

    public Set<SecurityIdentity> resolveIdentities(Long memberId) {
        if (isAnonymous(memberId)) {
            return Sets.newHashSet(AnonymousIdentity.getInstance());
        }

        HashSet<SecurityIdentity> memberIdentities = Sets
            .newHashSet(RegisteredMembersIdentity.getInstance(), new MemberIdentity(memberId));
        if (roleService.hasAdministratorRole(memberId)) {
            memberIdentities.add(AdministratorGroupIdentity.getInstance());
        }

        return ImmutableSet.copyOf(memberIdentities);
    }

    private boolean isAnonymous(Long memberId) {
        return memberId == 0;
    }

}
