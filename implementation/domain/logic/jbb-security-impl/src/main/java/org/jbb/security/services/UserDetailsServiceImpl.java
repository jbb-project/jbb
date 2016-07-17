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

import org.jbb.lib.core.vo.Login;
import org.jbb.security.SecurityConfig;
import org.jbb.security.dao.SecurityAccountDetailsRepository;
import org.jbb.security.entities.SecurityAccountDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private SecurityAccountDetailsRepository repository;

    @Autowired
    public UserDetailsServiceImpl(SecurityAccountDetailsRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(transactionManager = SecurityConfig.JTA_MANAGER, readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        SecurityAccountDetailsEntity securityDetails = repository.findByLogin(Login.builder().value(login).build());

        if (securityDetails == null) {
            throw new UsernameNotFoundException(String.format("Member with login '%s' not found", login));
        }

        return getUserDetails(securityDetails);
    }

    private UserDetails getUserDetails(SecurityAccountDetailsEntity securityDetails) {
        return new User(
                securityDetails.getLogin().getValue(),
                securityDetails.getCurrentPassword().getPassword(),
                securityDetails.isAccountEnabled(),
                !securityDetails.isAccountExpired(),
                true, //credentials not expired TODO
                !securityDetails.isAccountLocked(),
                Sets.newHashSet()
        );
    }
}
