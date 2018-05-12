/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.security;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.members.api.base.Member;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.jbb.security.api.privilege.PrivilegeService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityContentUserFactory {
    public static final String ADMIN_ROLE_NAME = "ROLE_ADMINISTRATOR";

    private static final boolean ALWAYS_ENABLED = true;
    private static final boolean ALWAYS_NON_EXPIRED = true;
    private static final boolean CREDENTIALS_ALWAYS_NON_EXPIRED = true;

    private final PrivilegeService privilegeService;
    private final MemberLockoutService memberLockoutService;

    public SecurityContentUser create(String passwordHash, Member member) {
        User user = new User(
                member.getUsername().getValue(),
                passwordHash,
                ALWAYS_ENABLED,
                ALWAYS_NON_EXPIRED,
                CREDENTIALS_ALWAYS_NON_EXPIRED,
            !memberLockoutService.isMemberHasActiveLock(member.getId()),
                resolveRoles(member.getId())
        );
        return new SecurityContentUser(user, member.getDisplayedName().toString(), member.getId());
    }

    private Collection<? extends GrantedAuthority> resolveRoles(Long memberId) {
        Set<GrantedAuthority> roles = Sets.newHashSet();
        if (privilegeService.hasAdministratorPrivilege(memberId)) {
            roles.add(new SimpleGrantedAuthority(ADMIN_ROLE_NAME));
        }
        return roles;
    }
}
