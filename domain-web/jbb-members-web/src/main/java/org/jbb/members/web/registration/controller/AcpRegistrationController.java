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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jbb.members.api.registration.RegistrationService;
import org.jbb.members.web.registration.form.RegistrationSettingsForm;
import org.jbb.members.web.registration.logic.RegistrationSettingsErrorsBindingMapper;
import org.jbb.security.api.password.PasswordException;
import org.jbb.security.api.password.PasswordPolicy;
import org.jbb.security.api.password.PasswordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/acp/general/registration")
public class AcpRegistrationController {

    private static final String VIEW_NAME = "acp/general/registration";
    private static final String REGISTRATION_SETTINGS_FORM = "registrationSettingsForm";
    private static final String FORM_SAVED_FLAG = "registrationSettingsFormSaved";

    private final RegistrationSettingsErrorsBindingMapper errorMapper;
    private final RegistrationService registrationService;
    private final PasswordService passwordService;

    @RequestMapping(method = RequestMethod.GET)
    public String generalRegistrationGet(Model model,
                                         @ModelAttribute(REGISTRATION_SETTINGS_FORM) RegistrationSettingsForm form) {
        form.setEmailDuplicationAllowed(registrationService.isEmailDuplicationAllowed());

        PasswordPolicy passwordPolicy = passwordService.currentPolicy();
        form.setMinPassLength(passwordPolicy.getMinimumLength());
        form.setMaxPassLength(passwordPolicy.getMaximumLength());

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
            PasswordPolicy passwordPolicy = PasswordPolicy.builder()
                    .minimumLength(form.getMinPassLength())
                    .maximumLength(form.getMaxPassLength())
                    .build();
            passwordService.updatePolicy(passwordPolicy);
        } catch (PasswordException e) {
            errorMapper.map(e.getConstraintViolations(), bindingResult);
            log.debug("Exception during setting new password policy", e);
            redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, false);
            return VIEW_NAME;
        }
        registrationService.allowEmailDuplication(form.isEmailDuplicationAllowed());

        redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);
        return "redirect:/" + VIEW_NAME;
    }

}
