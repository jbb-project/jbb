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

import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.core.vo.Login;
import org.jbb.members.api.data.Member;
import org.jbb.members.api.service.MemberService;
import org.jbb.security.impl.password.dao.PasswordRepository;
import org.jbb.security.impl.password.model.PasswordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberService memberService;
    private final PasswordRepository passwordRepository;
    private final SecurityContentUserFactory securityContentUserFactory;

    @Autowired
    public UserDetailsServiceImpl(MemberService memberService, PasswordRepository repository,
                                  SecurityContentUserFactory securityContentUserFactory) {
        this.memberService = memberService;
        this.passwordRepository = repository;
        this.securityContentUserFactory = securityContentUserFactory;
    }

    private static UserDetails throwUserNotFoundException(String reason) {
        throw new UsernameNotFoundException(reason);
    }

    private UserDetails getUserDetails(PasswordEntity passwordEntity) {
        Optional<Member> memberData = memberService.getMemberWithLogin(passwordEntity.getLogin());
        if (!memberData.isPresent()) {
            log.error("Some inconsistency of data detected! Password data exist for username '{}' but member data not", passwordEntity.getLogin());
            throwUserNotFoundException(String.format("Member with login '%s' not found", passwordEntity.getLogin()));
        }

        return securityContentUserFactory.create(passwordEntity, memberData.get());
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
