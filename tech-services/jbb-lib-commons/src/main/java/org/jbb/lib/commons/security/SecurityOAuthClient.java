/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.commons.security;

import org.apache.commons.collections.set.UnmodifiableSet;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityOAuthClient {

    private String clientId;
    private Set<OAuthScope> grantedScopes;

    public static SecurityOAuthClient of(String clientId, Set<String> scopes) {
        return new SecurityOAuthClient(clientId, UnmodifiableSet.decorate(
                scopes.stream().map(OAuthScope::ofName)
                        .flatMap(o -> o.isPresent() ? Stream.of(o.get()) : Stream.empty())
                        .collect(Collectors.toSet())));
    }
}
