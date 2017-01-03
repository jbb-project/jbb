/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.test;

import com.google.common.collect.Sets;

import org.jbb.lib.core.security.SecurityContentUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

@Component
public class TestbedSecurityContextRepository extends HttpSessionSecurityContextRepository {

    private static User getAnonUser() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_ANONYMOUS");

        return new User("Anonymous", "anon", Sets.newHashSet(simpleGrantedAuthority));
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        SecurityContext context = super.loadContext(requestResponseHolder);

        Authentication authentication = context.getAuthentication();

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            User user = (User) authentication.getPrincipal();
            SecurityContentUser securityContentUser = new SecurityContentUser(user, user.getUsername(), Long.valueOf(user.hashCode()));

            UsernamePasswordAuthenticationToken newAuthentication =
                    new UsernamePasswordAuthenticationToken(securityContentUser, authentication.getCredentials(), securityContentUser.getAuthorities());
            context.setAuthentication(newAuthentication);
        } else {
            AnonUserDetails anonUserDetails = new AnonUserDetails();
            AnonymousAuthenticationToken anonToken =
                    new AnonymousAuthenticationToken("ANON", anonUserDetails, anonUserDetails.getAuthorities());
            context.setAuthentication(anonToken);
        }

        return context;
    }

    private class AnonUserDetails extends SecurityContentUser {

        public AnonUserDetails() {
            super(getAnonUser(), "Anonymous", 0L);
        }

    }
}
