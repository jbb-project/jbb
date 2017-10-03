/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.install.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jbb.install.InstallationData;
import org.jbb.system.api.install.InstallationService;
import org.jbb.system.web.install.form.InstallForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
@RequestMapping("/install")
@RequiredArgsConstructor
public class InstallationController {

    private static final String VIEW_NAME = "install";
    private static final String INSTALL_FORM = "installForm";

    private final InstallationService installationService;

    @RequestMapping(method = RequestMethod.GET)
    public String installGet(Model model) {
        model.addAttribute(INSTALL_FORM, new InstallForm());
        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String installPost(@ModelAttribute(INSTALL_FORM) InstallForm form) {
        //TODO validate install form
        installationService.install(transformToInstallationData(form));
        return "redirect:/" + VIEW_NAME;
    }

    private InstallationData transformToInstallationData(InstallForm form) {
        return InstallationData.builder()
            .adminUsername(form.getAdminUsername())
            .adminDisplayedName(form.getAdminDisplayedName())
            .adminEmail(form.getAdminEmail())
            .adminPassword(form.getAdminPassword())
            .boardName(form.getBoardName())
            .build();
    }

}
