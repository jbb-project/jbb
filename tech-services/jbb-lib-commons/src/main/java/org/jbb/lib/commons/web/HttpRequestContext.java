/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.commons.web;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HttpRequestContext {

    private final UserDetailsSource userDetailsSource;


    // TODO throw UsernameNotFoundException?
    public Optional<Long> getCurrentMemberId() {
        SecurityContentUser securityContentUser = userDetailsSource.getFromApplicationContext();
        if (securityContentUser != null) {
            Long memberId = securityContentUser.getUserId();
            if (memberId != null && memberId != 0) {
                return Optional.of(memberId);
            }
        }
        return Optional.empty();
    }

}
