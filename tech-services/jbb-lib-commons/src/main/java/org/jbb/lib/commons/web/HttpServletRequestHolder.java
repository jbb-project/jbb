/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.commons.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HttpServletRequestHolder {
    public HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        log.debug("Not called in the context of an HTTP request");
        return null;
    }

    public String getCurrentIpAddress() {
        HttpServletRequest currentHttpRequest = getCurrentHttpRequest();
        if (currentHttpRequest != null) {
            return currentHttpRequest.getRemoteAddr();
        }
        WebAuthenticationDetails authDetails = getWebAuthenticationDetails();
        if (authDetails != null) {
            return authDetails.getRemoteAddress();
        }
        return null;
    }

    public String getCurrentSessionId() {
        HttpServletRequest currentHttpRequest = getCurrentHttpRequest();
        if (currentHttpRequest != null) {
            HttpSession session = currentHttpRequest.getSession(false);
            if (session != null) {
                return session.getId();
            }
        }
        WebAuthenticationDetails authDetails = getWebAuthenticationDetails();
        if (authDetails != null) {
            return authDetails.getSessionId();
        }
        return null;
    }

    private WebAuthenticationDetails getWebAuthenticationDetails() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null) {
            Authentication authentication = context.getAuthentication();
            if (authentication != null) {
                Object details = authentication.getDetails();
                if (details instanceof WebAuthenticationDetails) {
                    return (WebAuthenticationDetails) details;
                }
            }
        }
        return null;
    }

}
