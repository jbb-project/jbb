/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.oauthclient;

import com.google.common.collect.Sets;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.ScenarioSteps;

import org.apache.commons.lang3.RandomStringUtils;
import org.jbb.e2e.serenity.rest.commons.AuthRestSteps;
import org.jbb.e2e.serenity.rest.commons.TestOAuthClient;
import org.jbb.e2e.serenity.web.EndToEndWebStories;
import org.jbb.lib.commons.security.OAuthScope;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SetupOAuthSteps extends ScenarioSteps {

    @Steps
    AuthRestSteps authRestSteps;

    @Steps
    OAuthClientResourceSteps oAuthClientResourceSteps;

    @Step
    public TestOAuthClient create_client_with_scope(OAuthScope scope) {
        authRestSteps.sign_in_as_admin_for_every_request();
        OAuthClientDto clientDto = OAuthClientDto.builder()
                .clientId(scope.name().toLowerCase())
                .displayedName(scope.name())
                .grantTypes(Sets.newHashSet("CLIENT_CREDENTIALS"))
                .scopes(Sets.newHashSet(scope.getScopeName()))
                .build();

        SecretOAuthClientDto client = oAuthClientResourceSteps.create_oauth_client(clientDto)
                .as(SecretOAuthClientDto.class);
        authRestSteps.remove_authorization_headers_from_request();
        return new TestOAuthClient(client.getClientId(), client.getClientSecret());
    }

    public TestOAuthClient create_client_with_all_scopes_except(OAuthScope... excludedScopes) {
        authRestSteps.sign_in_as_admin_for_every_request();
        Set<OAuthScope> excludedScopeSet = Arrays.stream(excludedScopes).collect(Collectors.toSet());
        String clientId = "testclient_" + RandomStringUtils.randomAlphabetic(6);
        Set<String> requestedScopes = Arrays.stream(OAuthScope.values())
                .filter(scope -> !excludedScopeSet.contains(scope))
                .map(OAuthScope::getScopeName)
                .collect(Collectors.toSet());
        OAuthClientDto clientDto = OAuthClientDto.builder()
                .clientId(clientId)
                .displayedName(clientId)
                .grantTypes(Sets.newHashSet("CLIENT_CREDENTIALS"))
                .scopes(requestedScopes)
                .build();

        SecretOAuthClientDto client = oAuthClientResourceSteps.create_oauth_client(clientDto)
                .as(SecretOAuthClientDto.class);
        authRestSteps.remove_authorization_headers_from_request();
        return new TestOAuthClient(client.getClientId(), client.getClientSecret());
    }

    public EndToEndWebStories.RollbackAction delete_oauth_client(TestOAuthClient client) {
        return () -> {
            authRestSteps.sign_in_as_admin_for_every_request();
            oAuthClientResourceSteps.delete_oauth_client(client.getClientId());
            authRestSteps.remove_authorization_headers_from_request();
        };
    }
}
