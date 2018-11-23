/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.oauth.scope;

import org.jbb.lib.commons.security.OAuthScope;
import org.springframework.stereotype.Component;

@Component
public class ScopeTranslator {

    public OAuthScopeDto toDto(OAuthScope scope) {
        return OAuthScopeDto.builder()
                .name(scope.getScopeName())
                .description(scope.getDescription())
                .build();
    }
}
