/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc.security;

import com.google.common.collect.Sets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class RefreshableSecurityContextRepository extends HttpSessionSecurityContextRepository {
    @Autowired(required = false)
    private UserDetailsService userDetailsService;


    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        SecurityContext context = super.loadContext(requestResponseHolder);

        Authentication authentication = context.getAuthentication();

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UserDetails userDetails = this.createNewUserDetailsFromPrincipal((User) authentication.getPrincipal());

            UsernamePasswordAuthenticationToken newAuthentication =
                    new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
            context.setAuthentication(newAuthentication);
        } else {
            AnonUserDetails anonUserDetails = new AnonUserDetails();
            AnonymousAuthenticationToken anonToken =
                    new AnonymousAuthenticationToken("ANON", anonUserDetails, anonUserDetails.getAuthorities());
            context.setAuthentication(anonToken);
        }

        return context;
    }

    private UserDetails createNewUserDetailsFromPrincipal(User principal) {
        return userDetailsService.loadUserByUsername(principal.getUsername());
    }

    private class AnonUserDetails implements UserDetails {

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_ANONYMOUS");
            return Sets.newHashSet(simpleGrantedAuthority);
        }

        @Override
        public String getPassword() {
            return "anon";
        }

        @Override
        public String getUsername() {
            return "Anonymous";
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        public String getDisplayedName() {
            return "Anonymous"; //NOSONAR
        }
    }
}
