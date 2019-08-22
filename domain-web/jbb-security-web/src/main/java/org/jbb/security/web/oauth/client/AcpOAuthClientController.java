/*
 * Copyright (C) 2019 the original author or authors.
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
import org.jbb.security.api.oauth.OAuthClientException;
import org.jbb.security.api.oauth.OAuthClientNotFoundException;
import org.jbb.security.api.oauth.OAuthClientsService;
import org.jbb.security.api.oauth.SecretOAuthClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

import static org.jbb.security.web.oauth.client.AcpOAuthClientsController.OAUTH_CLIENT_ACP_VIEW;

@Controller
@RequiredArgsConstructor
@RequestMapping("/acp/system/oauth/clients")
public class AcpOAuthClientController {

    private static final String OAUTH_CLIENTS_URL = "acp/system/oauth/clients";
    private static final String OAUTH_CLIENT_DETAILS_ACP_VIEW = "acp/system/oauth-client";
    private static final String CLIENT_FORM = "clientForm";
    private static final String FORM_SAVED_FLAG = "clientFormSaved";
    private static final String NEW_CLIENT_STATE = "newClientState";
    private static final String SUPPORTED_SCOPES = "supportedScopes";
    private static final String CLIENT_ID_FORM = "clientIdForm";
    private static final String SHOW_SECRET_FLAG = "showSecret";

    private final OAuthClientsService oAuthClientsService;

    private final OAuthClientFormTranslator formTranslator;
    private final OAuthClientErrorsBindingMapper errorsBindingMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String oauthClientGet(@RequestParam(value = "id", required = false) String clientId,
                                 Model model) throws OAuthClientNotFoundException {
        if (StringUtils.isNotBlank(clientId) && !model.containsAttribute(FORM_SAVED_FLAG)) {
            OAuthClient client = oAuthClientsService.getClientChecked(clientId);
            model.addAttribute(CLIENT_FORM, formTranslator.toForm(client));
        } else if (!model.containsAttribute(FORM_SAVED_FLAG)) {
            model.addAttribute(CLIENT_FORM, formTranslator.toNewForm());
        } else if (model.containsAttribute(CLIENT_FORM)) {
            model.addAttribute(CLIENT_FORM, formTranslator.normalizeForm((OAuthClientForm) model.asMap().get(CLIENT_FORM)));
        }
        model.addAttribute(NEW_CLIENT_STATE, setNewClientStateFlag(clientId, model));
        model.addAttribute(SUPPORTED_SCOPES, Lists.newArrayList(OAuthScope.values()));
        OAuthClientIdForm idForm = new OAuthClientIdForm();
        idForm.setId(clientId);
        model.addAttribute(CLIENT_ID_FORM, idForm);
        return OAUTH_CLIENT_DETAILS_ACP_VIEW;
    }

    private boolean formNotSaved(Model model) {
        return model.containsAttribute(FORM_SAVED_FLAG) && !((Boolean) model.asMap().get(FORM_SAVED_FLAG));
    }

    private boolean setNewClientStateFlag(String clientId, Model model) {
        if (formNotSaved(model)) {
            return true;
        }
        return StringUtils.isBlank(clientId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String oauthClientPost(@ModelAttribute(CLIENT_FORM) OAuthClientForm form, Model model,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) throws OAuthClientNotFoundException {
        OAuthClient client = formTranslator.toClient(form);
        boolean addingMode = form.isAddingMode();
        model.addAttribute(NEW_CLIENT_STATE, addingMode);
        try {
            if (addingMode) {
                client = oAuthClientsService.createClient(client);
                redirectAttributes.addFlashAttribute(SHOW_SECRET_FLAG, true);
                redirectAttributes.addFlashAttribute("secret", ((SecretOAuthClient) client).getClientSecret());
            } else {
                client = oAuthClientsService.updateClient(form.getClientId(), client);
            }
            redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);
        } catch (OAuthClientException ex) {
            errorsBindingMapper.map(ex.getConstraintViolations(), bindingResult);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + CLIENT_FORM, bindingResult);
            redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, false);
        }

        redirectAttributes.addFlashAttribute(CLIENT_FORM, form);

        if (StringUtils.isNotBlank(form.getClientId())) {
            redirectAttributes.addAttribute("id", client.getClientId());
        }

        return "redirect:/" + OAUTH_CLIENTS_URL;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String oauthClientDelete(@ModelAttribute(CLIENT_ID_FORM) OAuthClientIdForm form) throws OAuthClientNotFoundException {
        oAuthClientsService.removeClient(form.getId());
        return "redirect:/" + OAUTH_CLIENT_ACP_VIEW;
    }

    @RequestMapping(value = "/regenerate", method = RequestMethod.POST)
    public String oauthClientRegenerate(@ModelAttribute(CLIENT_ID_FORM) OAuthClientIdForm form,
                                        RedirectAttributes redirectAttributes) throws OAuthClientNotFoundException {
        String newSecret = oAuthClientsService.generateClientSecret(form.getId());
        redirectAttributes.addFlashAttribute(SHOW_SECRET_FLAG, true);
        redirectAttributes.addFlashAttribute("secret", newSecret);
        redirectAttributes.addAttribute("id", form.getId());
        return "redirect:/" + OAUTH_CLIENTS_URL;
    }

}
