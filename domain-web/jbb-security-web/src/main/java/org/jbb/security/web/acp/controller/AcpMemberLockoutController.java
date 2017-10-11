/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.acp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jbb.lib.mvc.SimpleErrorsBindingMapper;
import org.jbb.security.api.lockout.MemberLockoutException;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.jbb.security.api.lockout.MemberLockoutSettings;
import org.jbb.security.web.acp.form.UserLockSettingsForm;
import org.jbb.security.web.acp.translator.UserLockSettingsFormTranslator;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Component
@RequiredArgsConstructor
@RequestMapping("/acp/general/lockout")
public class AcpMemberLockoutController {

    private static final String MEMBER_LOCKOUT_ACP_VIEW_NAME = "acp/general/lockout";
    private static final String ACP_MEMBER_LOCK_SETTING_FORM = "lockoutSettingsForm";
    private static final String FORM_SAVED_FLAG = "lockoutSettingsFormSaved";

    private final MemberLockoutService memberLockoutService;
    private final UserLockSettingsFormTranslator translator;
    private final SimpleErrorsBindingMapper errorsBindingMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String userLockSettingsPanelGet(Model model) {

        MemberLockoutSettings settings = memberLockoutService.getLockoutSettings();

        UserLockSettingsForm form = new UserLockSettingsForm();
        form.setLockingEnabled(settings.isLockingEnabled());
        form.setFailedSignInAttemptsExpirationMinutes(
            settings.getFailedSignInAttemptsExpirationMinutes());
        form.setFailedAttemptsThreshold(settings.getFailedAttemptsThreshold());
        form.setLockoutDurationMinutes(settings.getLockoutDurationMinutes());

        model.addAttribute(ACP_MEMBER_LOCK_SETTING_FORM, form);
        return MEMBER_LOCKOUT_ACP_VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String userLockSettingsPanelPost(@ModelAttribute(ACP_MEMBER_LOCK_SETTING_FORM) UserLockSettingsForm form,
                                            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.debug("Lockout settings form error detected: {}", bindingResult.getAllErrors());
            return MEMBER_LOCKOUT_ACP_VIEW_NAME;
        }
        MemberLockoutSettings serviceSettings = translator.createSettingsModel(form);

        try {
            memberLockoutService.setLockoutSettings(serviceSettings);
        } catch (MemberLockoutException e) {
            log.debug("Setting lockout settings failed", e);
            errorsBindingMapper.map(e.getConstraintViolations(), bindingResult);
            return MEMBER_LOCKOUT_ACP_VIEW_NAME;
        }

        redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);

        return "redirect:/" + MEMBER_LOCKOUT_ACP_VIEW_NAME;
    }

}
