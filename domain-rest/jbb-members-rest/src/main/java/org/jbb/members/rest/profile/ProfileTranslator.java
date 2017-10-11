/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.profile;

import org.jbb.members.api.base.Member;
import org.springframework.stereotype.Component;

@Component
public class ProfileTranslator {

    public ProfileDto toDto(Member member) {
        return ProfileDto.builder()
            .username(member.getUsername().getValue())
            .displayedName(member.getDisplayedName().getValue())
            .build();
    }
}
