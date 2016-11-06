/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.registration.controller;

import org.jbb.members.api.service.RegistrationService;
import org.jbb.members.web.registration.form.RegistrationSettingsForm;
import org.jbb.security.api.data.PasswordRequirements;
import org.jbb.security.api.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/acp/general/registration")
@Slf4j
public class AcpRegistrationController {
    private static final String VIEW_NAME = "acp/general/registration";
    private static final String REGISTRATION_SETTINGS_FORM = "registrationSettingsForm";
    private static final String FORM_SAVED_FLAG = "registrationSettingsFormSaved";

    private final RegistrationService registrationService;
    private final PasswordService passwordService;

    @Autowired
    public AcpRegistrationController(RegistrationService registrationService,
                                     PasswordService passwordService) {
        this.registrationService = registrationService;
        this.passwordService = passwordService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String generalRegistrationGet(Model model,
                                         @ModelAttribute(REGISTRATION_SETTINGS_FORM) RegistrationSettingsForm form) {
        form.setEmailDuplicationAllowed(registrationService.isEmailDuplicationAllowed());

        PasswordRequirements passwordRequirements = passwordService.currentRequirements();
        form.setMinPassLength(passwordRequirements.minimumLength().orElse(1));
        form.setMaxPassLength(passwordRequirements.maximumLength().orElse(999));

        model.addAttribute(REGISTRATION_SETTINGS_FORM, form);

        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String generalRegistrationPost(Model model,
                                          @ModelAttribute(REGISTRATION_SETTINGS_FORM) RegistrationSettingsForm form,
                                          BindingResult bindingResult,
                                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.debug("Registration settings form error detected: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, false);
            return VIEW_NAME;
        }
        try {
            PasswordRequirements passwordRequirements = new PasswordRequirements() {
                @Override
                public Optional<Integer> minimumLength() {
                    return Optional.of(form.getMinPassLength());
                }

                @Override
                public Optional<Integer> maximumLength() {
                    return Optional.of(form.getMaxPassLength());
                }
            };
            passwordService.updateRequirements(passwordRequirements);
        } catch (IllegalArgumentException e) {
            log.debug("Exception during setting new password requirements", e);
            redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, false);
            return VIEW_NAME;
        }
        registrationService.allowEmailDuplication(form.isEmailDuplicationAllowed());

        redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);
        return "redirect:/" + VIEW_NAME;
    }

}
