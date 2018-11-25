/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.api.oauth;

import com.google.common.collect.Sets;

import org.jbb.lib.commons.security.OAuthScope;

import java.util.Optional;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditOAuthClient {

    @NotBlank
    private String displayedName;

    private Optional<String> description;

    @NotNull
    @NotEmpty
    private Set<GrantType> grantTypes;

    @NotNull
    @NotEmpty
    private Set<OAuthScope> scopes;

    @NotNull
    @Builder.Default
    private Set<String> redirectUris = Sets.newTreeSet();

}
