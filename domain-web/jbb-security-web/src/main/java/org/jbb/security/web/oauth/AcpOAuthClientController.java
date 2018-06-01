/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.oauth;

import org.jbb.security.api.oauth.OAuthClientsService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@RequestMapping("/acp/system/oauth")
public class AcpOAuthClientController {

    private static final String OAUTH_CLIENT_ACP_VIEW = "acp/system/oauth";

    private final OAuthClientsService oAuthClientsService;

    @RequestMapping(method = RequestMethod.GET)
    public String oauthClientsGet() {
        return OAUTH_CLIENT_ACP_VIEW;
    }

}
