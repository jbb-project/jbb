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

import com.google.common.collect.Maps;

import org.jbb.lib.commons.security.OAuthScope;
import org.jbb.security.api.oauth.GrantType;
import org.jbb.security.api.oauth.OAuthClient;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OAuthClientFormTranslator {

    public OAuthClientForm toForm(OAuthClient client) {
        OAuthClientForm form = new OAuthClientForm();
        form.setClientId(client.getClientId());
        form.setDisplayedName(client.getDisplayedName());
        form.setDescription(client.getDescription().orElse(null));
        form.setGrantTypes(toGrantTypesMap(client));
        form.setScopes(toScopeMap(client));
        form.setRedirectUris(toRedirectUrisString(client));
        form.setAddingMode(false);
        return form;
    }

    private String toRedirectUrisString(OAuthClient client) {
        return client.getRedirectUris().stream()
                .collect(Collectors.joining("\n"));
    }

    private Map<String, Boolean> toGrantTypesMap(OAuthClient client) {
        Map<String, Boolean> result = Maps.newHashMap();
        for (GrantType grantType : GrantType.values()) {
            result.put(grantType.getName(), client.getGrantTypes().contains(grantType));
        }
        return result;
    }

    private Map<String, Boolean> toScopeMap(OAuthClient client) {
        Map<String, Boolean> result = Maps.newHashMap();
        for (OAuthScope scope : OAuthScope.values()) {
            result.put(scope.getScopeName(), client.getScopes().contains(scope));
        }
        return result;
    }

    public OAuthClientForm toNewForm() {
        OAuthClientForm form = new OAuthClientForm();
        form.setGrantTypes(toEmptyGrantTypesMap());
        form.setScopes(toEmptyScopeMap());
        form.setAddingMode(true);
        return form;
    }

    private Map<String, Boolean> toEmptyGrantTypesMap() {
        Map<String, Boolean> result = Maps.newHashMap();
        for (GrantType grantType : GrantType.values()) {
            result.put(grantType.getName(), false);
        }
        return result;
    }

    private Map<String, Boolean> toEmptyScopeMap() {
        Map<String, Boolean> result = Maps.newHashMap();
        for (OAuthScope scope : OAuthScope.values()) {
            result.put(scope.getScopeName(), false);
        }
        return result;
    }
}
