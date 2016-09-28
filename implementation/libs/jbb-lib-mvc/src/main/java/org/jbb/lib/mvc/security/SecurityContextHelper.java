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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SecurityContextHelper {
    @Autowired
    private RefreshableSecurityContextRepository securityContextRepository;

    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        securityContextRepository.loadContext(new HttpRequestResponseHolder(request, response));
    }
}
