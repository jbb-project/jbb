/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.signin;

import org.jbb.security.api.signin.SignInSettingsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/acp/general/signin")
public class AcpSignInSettingsController {

    private static final String LOCK_BROWSER_ACP_VIEW = "acp/general/signin";
    private static final String LOCKS_SEARCH_FORM = "signInSettingsForm";
    private static final String FORM_SAVED_FLAG = "signInSettingsFormSent";

    private final SignInSettingsService signInSettingsService;

    @RequestMapping(method = RequestMethod.GET)
    public String signInSettingsGet(Model model) {
        return LOCK_BROWSER_ACP_VIEW;
    }

}
