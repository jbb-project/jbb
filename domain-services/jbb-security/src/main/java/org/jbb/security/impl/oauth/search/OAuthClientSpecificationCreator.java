/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.oauth.search;

import org.jbb.security.api.oauth.OAuthClientSearchCriteria;
import org.jbb.security.impl.oauth.model.OAuthClientEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import static org.jbb.security.impl.oauth.search.OAuthClientSpecifications.withClientId;
import static org.jbb.security.impl.oauth.search.OAuthClientSpecifications.withDisplayedName;

@Component
public class OAuthClientSpecificationCreator {

    public Specification<OAuthClientEntity> createSpecification(OAuthClientSearchCriteria criteria) {
        return Specification.where(withClientId(criteria.getClientId())
        ).and(withDisplayedName(criteria.getDisplayedName()));
    }
}
