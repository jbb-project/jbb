/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.install.logic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.commons.preinstall.JbbNoInstalledException;
import org.jbb.system.api.install.InstallationService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
@Order(5)
@RequiredArgsConstructor
public class PreInstallationApiInterceptor extends HandlerInterceptorAdapter {

    private final InstallationService installationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        if (!installationService.isInstalled() && request.getRequestURI().startsWith("/api/")) {
            throw new JbbNoInstalledException();
        }
        return true;
    }
}
