/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.me;

import org.jbb.lib.commons.security.OAuthScope;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.SecurityOAuthClient;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.commons.web.HttpServletRequestHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MeDataProvider {
    private final UserDetailsSource userDetailsSource;
    private final HttpServletRequestHolder requestHolder;

    public MeDataDto getMeData() {
        return MeDataDto.builder()
                .sessionId(requestHolder.getCurrentSessionId())
                .currentMember(getMemberContext())
                .currentOAuthClient(getClientContext())
                .build();
    }

    private MemberContextDto getMemberContext() {
        SecurityContentUser securityContentUser = userDetailsSource.getFromApplicationContext();
        if (securityContentUser == null) {
            return null;
        }
        return MemberContextDto.builder()
                .memberId(securityContentUser.getUserId())
                .administrator(isAdministrator(securityContentUser.getAuthorities()))
                .build();
    }

    private OAuthClientContextDto getClientContext() {
        SecurityOAuthClient client = userDetailsSource.getOAuthClient();
        if (client == null) {
            return null;
        }
        return OAuthClientContextDto.builder()
                .clientId(client.getClientId())
                .approvedScopes(client.getGrantedScopes().stream()
                        .map(OAuthScope::getScopeName)
                        .collect(Collectors.toSet()))
                .build();
    }

    private Boolean isAdministrator(Collection<GrantedAuthority> authorities) {
        return authorities.stream()
                .anyMatch(x -> x.equals(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR")));
    }

}
