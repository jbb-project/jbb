/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.commons.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UserDetailsSource {

    @PostConstruct
    public void setStrategy() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    public SecurityContentUser getFromApplicationContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof SecurityContentUser) {
                return (SecurityContentUser) principal;
            }
            if (authentication instanceof OAuth2Authentication) {
                Authentication userAuthentication = ((OAuth2Authentication) authentication).getUserAuthentication();
                if (userAuthentication instanceof SecurityContentUser) {
                    return (SecurityContentUser) userAuthentication;
                }
            }
        }
        return null;
    }

    public SecurityOAuthClient getOAuthClientFromApplicationContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication instanceof OAuth2Authentication) {
                OAuth2Request request = ((OAuth2Authentication) authentication).getOAuth2Request();
                return SecurityOAuthClient.of(request.getClientId(), request.getScope());
            }
        }
        return null;
    }

    public boolean isOAuthRequestWithoutUser() {
        return getFromApplicationContext() == null && getOAuthClientFromApplicationContext() != null;
    }
}
