/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.oauth.approval;

import org.jbb.lib.commons.security.OAuthScope;
import org.jbb.security.api.oauth.OAuthClient;
import org.jbb.security.api.oauth.OAuthClientsService;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/oauth/confirm_access")
@SessionAttributes("authorizationRequest")
public class OAuthApprovalController {
    private final OAuthClientsService oAuthClientsService;

    @RequestMapping(method = RequestMethod.GET)
    public String getConfirmAccess(Model model) throws Exception {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.asMap().get("authorizationRequest");
        if (authorizationRequest == null) {
            return "redirect:/";
        }
        String clientId = authorizationRequest.getClientId();
        OAuthClient oAuthClient = oAuthClientsService.getClientChecked(clientId);
        model.addAttribute("oAuthClient", oAuthClient.getDisplayedName());
        List<OAuthScope> requestedScopes = authorizationRequest.getScope().stream()
                .map(scope -> OAuthScope.ofName(scope).orElse(null)).collect(Collectors.toList());
        model.addAttribute("requestedScopes", requestedScopes);
        return "oauthApprove";
    }

}