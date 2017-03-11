/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.userdetails.logic;

import com.google.common.collect.Sets;

import org.jbb.lib.core.security.SecurityContentUser;
import org.jbb.members.api.data.Member;
import org.jbb.security.api.service.MemberLockoutService;
import org.jbb.security.api.service.RoleService;
import org.jbb.security.impl.password.model.PasswordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Component
public class SecurityContentUserFactory {
    public static final String ADMIN_ROLE_NAME = "ROLE_ADMINISTRATOR";

    private static final boolean ALWAYS_ENABLED = true;
    private static final boolean ALWAYS_NON_EXPIRED = true;
    private static final boolean CREDENTIALS_ALWAYS_NON_EXPIRED = true;

    private final RoleService roleService;
    private final MemberLockoutService memberLockoutService;

    @Autowired
    public SecurityContentUserFactory(RoleService roleService, MemberLockoutService memberLockoutService) {
        this.roleService = roleService;
        this.memberLockoutService = memberLockoutService;
    }

    public SecurityContentUser create(PasswordEntity passwordEntity, Member member) {
        User user = new User(
                member.getUsername().getValue(),
                passwordEntity.getPassword(),
                ALWAYS_ENABLED,
                ALWAYS_NON_EXPIRED,
                CREDENTIALS_ALWAYS_NON_EXPIRED,
                !memberLockoutService.isMemberHasLock(member.getId()),
                resolveRoles(member.getId())
        );
        return new SecurityContentUser(user, member.getDisplayedName().toString(), member.getId());
    }

    private Collection<? extends GrantedAuthority> resolveRoles(Long memberId) {
        Set<GrantedAuthority> roles = Sets.newHashSet();
        if (roleService.hasAdministratorRole(memberId)) {
            roles.add(new SimpleGrantedAuthority(ADMIN_ROLE_NAME));
        }
        return roles;
    }
}
