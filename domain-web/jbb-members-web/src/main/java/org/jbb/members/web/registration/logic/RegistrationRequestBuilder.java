/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.registration.logic;

import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.IPAddress;
import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.registration.RegistrationRequest;
import org.jbb.members.web.registration.form.RegisterForm;
import org.springframework.stereotype.Component;

@Component
public class RegistrationRequestBuilder {

    public RegistrationRequest buildRequest(RegisterForm form, IPAddress ipAddress) {
        String resolvedPassword = form.getPassword() == null ? "" : form.getPassword();
        String resolvedPasswordAgain =
                form.getPasswordAgain() == null ? "" : form.getPasswordAgain();
        return RegistrationRequest.builder()
                .username(Username.builder().value(form.getUsername()).build())
                .displayedName(DisplayedName.builder().value(form.getDisplayedName()).build())
                .email(Email.builder().value(form.getEmail()).build())
                .password(Password.builder().value(resolvedPassword.toCharArray()).build())
                .passwordAgain(Password.builder().value(resolvedPasswordAgain.toCharArray()).build())
                .ipAddress(ipAddress)
                .build();
    }

}
