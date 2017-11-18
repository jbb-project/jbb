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
import org.jbb.members.api.registration.RegistrationMetaData;
import org.springframework.stereotype.Component;

@Component
public class MemberPublicTranslator {

    public MemberPublicDto toDto(MemberRegistrationAware memberRegistrationAware) {
        return MemberPublicDto.builder()
            .id(memberRegistrationAware.getId())
            .displayedName(memberRegistrationAware.getDisplayedName().getValue())
            .joinDateTime(memberRegistrationAware.getRegistrationMetaData().getJoinDateTime())
            .build();
    }

    public MemberPublicDto toDto(Member member, RegistrationMetaData registrationMetaData) {
        return MemberPublicDto.builder()
            .id(member.getId())
            .displayedName(member.getDisplayedName().getValue())
            .joinDateTime(registrationMetaData.getJoinDateTime())
            .build();
    }
}
