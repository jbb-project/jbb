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

import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.ProfileDataToChange;
import org.jbb.members.api.registration.RegistrationMetaData;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProfileTranslator {

    public ProfileDto toDto(Member member,
                            RegistrationMetaData registrationMetaData) {
        return ProfileDto.builder()
                .username(member.getUsername().getValue())
                .displayedName(member.getDisplayedName().getValue())
                .joinDateTime(registrationMetaData.getJoinDateTime())
                .build();
    }

    public ProfilePublicDto toPublicDto(Member member,
                                        RegistrationMetaData registrationMetaData) {
        return ProfilePublicDto.builder()
                .displayedName(member.getDisplayedName().getValue())
                .joinDateTime(registrationMetaData.getJoinDateTime())
                .build();
    }

    public ProfileDataToChange toModel(UpdateProfileDto updateProfileDto) {
        return ProfileDataToChange.builder()
                .displayedName(Optional.of(
                        DisplayedName.builder().value(updateProfileDto.getDisplayedName()).build()
                ))
                .build();
    }

}
