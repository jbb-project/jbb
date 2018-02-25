/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.registration;

import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.IPAddress;
import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.registration.RegistrationRequest;
import org.springframework.stereotype.Component;

@Component
public class RegistrationRequestTranslator {

    public RegistrationRequest toModel(RegistrationRequestDto dto, IPAddress ipAddress) {
        String nullSafePassword = StringUtils.defaultIfEmpty(dto.getPassword(), StringUtils.EMPTY);
        return RegistrationRequest.builder()
                .username(Username.builder().value(dto.getUsername()).build())
                .displayedName(DisplayedName.builder().value(dto.getDisplayedName()).build())
                .email(Email.builder().value(dto.getEmail()).build())
                .password(Password.builder().value(nullSafePassword.toCharArray()).build())
                .passwordAgain(Password.builder().value(nullSafePassword.toCharArray()).build())
                .ipAddress(ipAddress)
                .build();
    }
}
