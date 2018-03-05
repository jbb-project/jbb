/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.install;

import org.jbb.install.InstallationData;
import org.jbb.lib.mvc.SimpleErrorsBindingMapper;
import org.jbb.system.api.install.InstallationDataException;
import org.jbb.system.api.install.InstallationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/install")
@RequiredArgsConstructor
public class InstallationController {

    private static final String VIEW_NAME = "install";
    private static final String INSTALL_FORM = "installForm";

    private final InstallationDataTranslator installationDataTranslator;
    private final InstallationService installationService;

    private final SimpleErrorsBindingMapper errorsBindingMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String installGet(Model model) {
        model.addAttribute(INSTALL_FORM, new InstallForm());
        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String installPost(Model model, @ModelAttribute(INSTALL_FORM) InstallForm form, BindingResult bindingResult) {
        InstallationData installationData = installationDataTranslator.transform(form);
        try {
            installationService.install(installationData);
        } catch (InstallationDataException e) {
            errorsBindingMapper.map(e.getConstraintViolations(), bindingResult);
            model.addAttribute(INSTALL_FORM, form);
            return VIEW_NAME;
        }
        return "redirect:/" + VIEW_NAME;
    }

}
