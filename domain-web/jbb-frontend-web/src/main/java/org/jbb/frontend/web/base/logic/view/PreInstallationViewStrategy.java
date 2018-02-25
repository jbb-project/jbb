/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.logic.view;

import org.jbb.system.api.install.InstallationService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

@Component
@Order(2)
@RequiredArgsConstructor
public class PreInstallationViewStrategy extends ReplacingViewStrategy {

    private final InstallationService installationService;

    @Override
    boolean canHandle(ModelAndView modelAndView) {
        return !installationService.isInstalled() && !"install".equals(modelAndView.getViewName())
                && !"swagger".equals(modelAndView.getViewName());
    }

    @Override
    void performHandle(ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/install");
    }
}
