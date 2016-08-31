/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.services;

import com.google.common.collect.Sets;

import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.core.vo.Login;
import org.jbb.members.api.model.Member;
import org.jbb.members.api.services.MemberService;
import org.jbb.security.dao.PasswordRepository;
import org.jbb.security.entities.PasswordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final boolean ALWAYS_ENABLED = true;
    private static final boolean ALWAYS_NON_EXPIRED = true;
    private static final boolean CREDENTIALS_ALWAYS_NON_EXPIRED = true;
    private static final boolean ALWAYS_NON_LOCKED = true;
    private static final Set<? extends GrantedAuthority> EMPTY_ROLES_SET = Sets.newHashSet();

    private final MemberService memberService;
    private final PasswordRepository passwordRepository;

    @Autowired
    public UserDetailsServiceImpl(MemberService memberService, PasswordRepository repository) {
        this.memberService = memberService;
        this.passwordRepository = repository;
    }

    private static UserDetails throwUserNotFoundException(String reason) {
        throw new UsernameNotFoundException(reason);
    }

    private UserDetails getUserDetails(PasswordEntity entity) {
        Optional<? extends Member> memberData = memberService.getMemberWIthLogin(entity.getLogin());
        if (!memberData.isPresent()) {
            throwUserNotFoundException(String.format("Member with login '%s' not found", entity.getLogin()));
        }
        return new SecurityContentUser(
                entity.getLogin().getValue(),
                memberData.get().getDisplayedName().toString(),
                entity.getPassword(),
                ALWAYS_ENABLED,
                ALWAYS_NON_EXPIRED,
                CREDENTIALS_ALWAYS_NON_EXPIRED,
                ALWAYS_NON_LOCKED,
                EMPTY_ROLES_SET
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) {
        if (StringUtils.isEmpty(login)) {
            throwUserNotFoundException("Login cannot be blank");
        }

        Optional<PasswordEntity> passwordEntity = passwordRepository.findTheNewestByLogin(Login.builder().value(login).build());

        if (!passwordEntity.isPresent()) {
            return throwUserNotFoundException(String.format("Member with login '%s' not found", login));
        }

        return getUserDetails(passwordEntity.get());
    }
}
