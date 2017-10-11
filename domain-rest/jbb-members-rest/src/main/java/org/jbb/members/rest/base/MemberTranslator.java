/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.base;

import org.jbb.members.api.base.Member;
import org.jbb.members.api.registration.MemberRegistrationAware;
import org.springframework.stereotype.Component;

@Component
public class MemberTranslator {

    public MemberDto toDto(Member member) {
        return MemberDto.builder()
            .id(member.getId())
            .username(member.getUsername().getValue())
            .displayedName(member.getDisplayedName().getValue())
            .email(member.getEmail().getValue())
            .build();
    }

    public MemberPublicDto toPublicDto(MemberRegistrationAware memberRegistrationAware) {
        return MemberPublicDto.builder()
            .id(memberRegistrationAware.getId())
            .displayedName(memberRegistrationAware.getDisplayedName().getValue())
            .joinDateTime(memberRegistrationAware.getRegistrationMetaData().getJoinDateTime())
            .build();
    }
}
