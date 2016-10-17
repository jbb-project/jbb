/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.userdetails.logic;

import com.google.common.collect.Sets;

import org.jbb.lib.core.vo.Username;
import org.jbb.members.api.data.Member;
import org.jbb.security.api.service.RoleService;
import org.jbb.security.api.service.UserLockService;
import org.jbb.security.impl.password.model.PasswordEntity;
import org.jbb.security.impl.userdetails.data.SecurityContentUser;
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

    private static final boolean ALWAYS_NON_EXPIRED = true;
    private static final boolean CREDENTIALS_ALWAYS_NON_EXPIRED = true;
    private static final boolean ALWAYS_NON_LOCKED = true;

    private final RoleService roleService;
    private UserLockService userLockService;

    @Autowired
    public SecurityContentUserFactory(UserLockService userLockService, RoleService roleService) {
        this.userLockService = userLockService;
        this.roleService = roleService;
    }


    public SecurityContentUser create(PasswordEntity passwordEntity, Member member) {
        User user = new User(
                passwordEntity.getUsername().getValue(),
                passwordEntity.getPassword(),
                userLockService.isUserHasAccountLock(member.getUsername()),
                ALWAYS_NON_EXPIRED,
                CREDENTIALS_ALWAYS_NON_EXPIRED,
                ALWAYS_NON_LOCKED,
                resolveRoles(passwordEntity.getUsername())
        );
        return new SecurityContentUser(user, member.getDisplayedName().toString());
    }

    private Collection<? extends GrantedAuthority> resolveRoles(Username username) {
        Set<GrantedAuthority> roles = Sets.newHashSet();
        if (roleService.hasAdministratorRole(username)) {
            roles.add(new SimpleGrantedAuthority(ADMIN_ROLE_NAME));
        }
        return roles;
    }
}
