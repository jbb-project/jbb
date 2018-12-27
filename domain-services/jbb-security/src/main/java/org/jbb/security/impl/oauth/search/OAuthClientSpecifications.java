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

import org.apache.commons.lang3.StringUtils;
import org.jbb.security.impl.oauth.model.OAuthClientEntity;
import org.jbb.security.impl.oauth.model.OAuthClientEntity_;
import org.springframework.data.jpa.domain.Specification;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class OAuthClientSpecifications {

    public static Specification<OAuthClientEntity> withClientId(String clientId) {
        if (StringUtils.isNotBlank(clientId)) {
            return (root, cq, cb) ->
                    cb.like(cb.upper(root.get(OAuthClientEntity_.clientId)),
                            "%" + clientId.toUpperCase() + "%");
        } else {
            return null;
        }
    }

    public static Specification<OAuthClientEntity> withDisplayedName(String displayedName) {
        if (StringUtils.isNotBlank(displayedName)) {
            return (root, cq, cb) ->
                    cb.like(cb.upper(root.get(OAuthClientEntity_.displayedName)),
                            "%" + displayedName.toUpperCase() + "%");
        } else {
            return null;
        }
    }

}
