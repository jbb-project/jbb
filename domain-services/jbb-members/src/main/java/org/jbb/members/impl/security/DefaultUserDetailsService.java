/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.security;

import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.password.PasswordService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("userDetailsService")
@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {

    private final MemberService memberService;
    private final PasswordService passwordService;
    private final SecurityContentUserFactory securityContentUserFactory;

    private static UserDetails throwUserNotFoundException(String reason) {
        throw new UsernameNotFoundException(reason);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameString) {
        if (StringUtils.isEmpty(usernameString)) {
            throwUserNotFoundException("Username cannot be blank");
        }

        Username username = Username.builder().value(usernameString).build();
        Optional<Member> memberData = memberService.getMemberWithUsername(username);
        return memberData
                .map(member -> getUserDetails(username, member))
                .orElseGet(
                        () -> throwUserNotFoundException(
                                String.format("Member with username '%s' not found", username.getValue())));
    }

    private UserDetails getUserDetails(Username username, Member member) {
        Optional<String> passwordHash = passwordService.getPasswordHash(member.getId());
        if (!passwordHash.isPresent()) {
            return throwUserNotFoundException(String.format("Member with username '%s' not found", username));
        }

        return securityContentUserFactory.create(passwordHash.get(), member);
    }
}
