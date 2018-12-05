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
import org.jbb.security.api.oauth.OAuthClientNotFoundException;
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
import lombok.extern.slf4j.Slf4j;

import static org.jbb.security.web.oauth.client.AcpOAuthClientsController.OAUTH_CLIENT_ACP_VIEW;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/acp/system/oauth/clients")
public class AcpOAuthClientController {

    private static final String OAUTH_CLIENT_DETAILS_ACP_VIEW = "acp/system/oauth-client";
    private static final String CLIENT_FORM = "clientForm";
    private static final String FORM_SAVED_FLAG = "clientFormSaved";
    private static final String NEW_CLIENT_STATE = "newClientState";
    private static final String SUPPORTED_SCOPES = "supportedScopes";
    private static final String CLIENT_ID_FORM = "clientIdForm";

    private final OAuthClientsService oAuthClientsService;

    private final OAuthClientFormTranslator formTranslator;

    @RequestMapping(method = RequestMethod.GET)
    public String oauthClientGet(@RequestParam(value = "id", required = false) String clientId,
                                 Model model) throws OAuthClientNotFoundException {
        OAuthClientForm form;
        if (StringUtils.isNotBlank(clientId)) {
            OAuthClient client = oAuthClientsService.getClientChecked(clientId);
            form = formTranslator.toForm(client);
        } else {
            form = formTranslator.toNewForm();
        }
        model.addAttribute(CLIENT_FORM, form);
        model.addAttribute(NEW_CLIENT_STATE, StringUtils.isBlank(clientId));
        model.addAttribute(SUPPORTED_SCOPES, Lists.newArrayList(OAuthScope.values()));
        OAuthClientIdForm idForm = new OAuthClientIdForm();
        idForm.setId(clientId);
        model.addAttribute(CLIENT_ID_FORM, idForm);
        return OAUTH_CLIENT_DETAILS_ACP_VIEW;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String oauthClientPost(@ModelAttribute(CLIENT_FORM) OAuthClientForm form, Model model,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) throws OAuthClientNotFoundException {
        OAuthClient client = formTranslator.toClient(form);
        if (form.isAddingMode()) {
            oAuthClientsService.createClient(client);
        } else {
            oAuthClientsService.updateClient(form.getClientId(), client);
        }
        redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);
        return OAUTH_CLIENT_DETAILS_ACP_VIEW;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String oauthClientDelete(@ModelAttribute(CLIENT_ID_FORM) OAuthClientIdForm form,
                                    BindingResult bindingResult, RedirectAttributes redirectAttributes) throws OAuthClientNotFoundException {
        oAuthClientsService.removeClient(form.getId());
        return "redirect:/" + OAUTH_CLIENT_ACP_VIEW;
    }

    @RequestMapping(value = "/regenerate", method = RequestMethod.POST)
    public String oauthClientRegenerate(@ModelAttribute(CLIENT_ID_FORM) OAuthClientIdForm form,
                                        BindingResult bindingResult, RedirectAttributes redirectAttributes) throws OAuthClientNotFoundException {
        String newSecret = oAuthClientsService.generateClientSecret(form.getId());
        log.info("Client '{}' nas new secret: {}", form.getId(), newSecret);
        return "redirect:/acp/system/oauth/clients";
    }

}
