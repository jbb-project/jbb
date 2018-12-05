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

import org.jbb.lib.mvc.PageWrapper;
import org.jbb.security.api.oauth.OAuthClientSearchCriteria;
import org.jbb.security.api.oauth.OAuthClientsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/acp/system/oauth")
public class AcpOAuthClientsController {

    public static final String OAUTH_CLIENT_ACP_VIEW = "acp/system/oauth";
    private static final String OAUTH_CLIENT_SEARCH_FORM = "oauthClientSearchForm";
    private static final String FORM_SAVED_FLAG = "oauthClientSearchFormSent";

    private final OAuthClientsService oAuthClientsService;

    private final SearchClientCriteriaFactory criteriaFactory;
    private final OAuthClientRowTranslator rowTranslator;

    @RequestMapping(method = RequestMethod.GET)
    public String oauthClientsGet(Model model, @ModelAttribute(OAUTH_CLIENT_SEARCH_FORM) SearchOAuthClientForm form) {
        model.addAttribute(OAUTH_CLIENT_SEARCH_FORM, form);
        return OAUTH_CLIENT_ACP_VIEW;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String oauthClientsPost(@ModelAttribute(OAUTH_CLIENT_SEARCH_FORM) SearchOAuthClientForm form,
                                   Pageable pageable,
                                   BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        OAuthClientSearchCriteria criteria = criteriaFactory.buildCriteria(form, pageable);
        Page<OAuthClientRow> resultPage = oAuthClientsService.getClientsWithCriteria(criteria)
                .map(rowTranslator::toRow);

        redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);
        redirectAttributes
                .addFlashAttribute("resultPage", new PageWrapper<>(resultPage));
        redirectAttributes.addFlashAttribute(OAUTH_CLIENT_SEARCH_FORM, form);

        return "redirect:/" + OAUTH_CLIENT_ACP_VIEW;
    }

}
