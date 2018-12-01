/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.me;

import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.SecurityOAuthClient;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.springframework.stereotype.Component;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MeDataProvider {
    private final UserDetailsSource userDetailsSource;

    public MeDataDto getMeData() {
        return MeDataDto.builder()
                .memberId(getMemberId())
                .oAuthClientId(getOAuthClientId())
                .build();
    }

    private Long getMemberId() {
        return Optional.ofNullable(userDetailsSource.getFromApplicationContext())
                .map(SecurityContentUser::getUserId)
                .orElse(null);
    }

    private String getOAuthClientId() {
        return Optional.ofNullable(userDetailsSource.getOAuthClient())
                .map(SecurityOAuthClient::getClientId)
                .orElse(null);
    }
}
