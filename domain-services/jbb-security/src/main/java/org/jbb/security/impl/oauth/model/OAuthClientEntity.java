/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.oauth.model;

import org.hibernate.envers.Audited;
import org.jbb.lib.commons.security.OAuthScope;
import org.jbb.lib.db.domain.BaseEntity;
import org.jbb.security.api.oauth.GrantType;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Getter
@Setter
@Entity
@Audited
@Table(name = "JBB_OAUTH_CLIENTS")
@Builder
@EqualsAndHashCode(callSuper = true)
public class OAuthClientEntity extends BaseEntity {

    @NotBlank
    @Column(name = "client_id", unique = true)
    private String clientId;

    @NotBlank
    @Column(name = "client_secret")
    private String clientSecret;

    @NotBlank
    @Column(name = "displayed_name")
    private String displayedName;

    @NotNull
    @ElementCollection(targetClass = GrantType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "JBB_OAUTH_GRANT_TYPES", joinColumns = {@JoinColumn(name = "oauth_client_id")})
    @Column(name = "grant_type")
    private Set<GrantType> grantTypes;

    @NotNull
    @ElementCollection(targetClass = OAuthScope.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "JBB_OAUTH_SCOPES", joinColumns = {@JoinColumn(name = "oauth_client_id")})
    @Column(name = "scope")
    private Set<OAuthScope> scopes;

    @Tolerate
    OAuthClientEntity() {
        // for JPA
    }
}
