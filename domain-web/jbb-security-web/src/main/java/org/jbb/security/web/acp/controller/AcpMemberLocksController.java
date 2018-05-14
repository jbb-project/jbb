/*
 * Copyright (C) 2018 the original author or authors.
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
import org.jbb.security.api.lockout.MemberLock;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.jbb.security.web.acp.form.SearchLockForm;
import org.springframework.data.domain.Page;
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
@RequestMapping("/acp/members/locks")
public class AcpMemberLocksController {

    private static final String LOCK_BROWSER_ACP_VIEW = "acp/members/locks";
    private static final String LOCKS_SEARCH_FORM = "lockSearchForm";
    private static final String FORM_SAVED_FLAG = "lockSearchFormSaved";

    private final MemberLockoutService memberLockoutService;

    @RequestMapping(method = RequestMethod.GET)
    public String memberLocksGet(Model model,
        @ModelAttribute(LOCKS_SEARCH_FORM) SearchLockForm form) {
        model.addAttribute(LOCKS_SEARCH_FORM, form);
        return LOCK_BROWSER_ACP_VIEW;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String memberLocksPost(@ModelAttribute(LOCKS_SEARCH_FORM) SearchLockForm form,
        BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.debug("Lockout settings form error detected: {}", bindingResult.getAllErrors());
            return LOCK_BROWSER_ACP_VIEW;
        }
//        MemberLockoutSettings serviceSettings = translator.createSettingsModel(form);
        Page<MemberLock> resultPage = memberLockoutService.getLocksWithCriteria(null);

        redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);

        return "redirect:/" + LOCK_BROWSER_ACP_VIEW;
    }

}
