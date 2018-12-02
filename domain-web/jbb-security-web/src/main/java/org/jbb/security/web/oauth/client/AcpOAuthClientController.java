/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.oauth.client;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.commons.security.OAuthScope;
import org.jbb.security.api.oauth.OAuthClient;
import org.jbb.security.api.oauth.OAuthClientsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/acp/system/oauth/clients")
public class AcpOAuthClientController {

    private static final String OAUTH_CLIENT_DETAILS_ACP_VIEW = "acp/system/oauth-client";
    private static final String CLIENT_FORM = "clientForm";
    private static final String FORM_SAVED_FLAG = "clientFormSaved";
    private static final String NEW_CLIENT_STATE = "newClientState";
    private static final String SUPPORTED_SCOPES = "supportedScopes";

    private final OAuthClientsService oAuthClientsService;

    private final OAuthClientFormTranslator formTranslator;

    @RequestMapping(method = RequestMethod.GET)
    public String oauthClientGet(@RequestParam(value = "id", required = false) String clientId,
                                 Model model, @ModelAttribute(CLIENT_FORM) OAuthClientForm form) {
        if (StringUtils.isNotBlank(clientId)) {
            OAuthClient client = oAuthClientsService.getClient(clientId).orElseThrow(() -> new IllegalStateException(""));
            form = formTranslator.toForm(client);
        } else {
            form = formTranslator.toNewForm();
        }
        model.addAttribute(CLIENT_FORM, form);
        model.addAttribute(NEW_CLIENT_STATE, StringUtils.isBlank(clientId));
        model.addAttribute(SUPPORTED_SCOPES, Lists.newArrayList(OAuthScope.values()));
        return OAUTH_CLIENT_DETAILS_ACP_VIEW;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String oauthClientPost(@ModelAttribute(CLIENT_FORM) OAuthClientForm form,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        return OAUTH_CLIENT_DETAILS_ACP_VIEW;
    }

}
