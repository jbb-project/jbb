/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.base.logic;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

public class RefreshableSecurityContextRepository extends HttpSessionSecurityContextRepository {
    private final UserDetailsService userDetailsService;

    public RefreshableSecurityContextRepository(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        SecurityContext context = super.loadContext(requestResponseHolder);

        Authentication authentication = context.getAuthentication();

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UserDetails userDetails = this.createNewUserDetailsFromPrincipal((User) authentication.getPrincipal());

            UsernamePasswordAuthenticationToken newAuthentication =
                    new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
            context.setAuthentication(newAuthentication);
        }

        return context;
    }

    private UserDetails createNewUserDetailsFromPrincipal(User principal) {
        return userDetailsService.loadUserByUsername(principal.getUsername());
    }
}
