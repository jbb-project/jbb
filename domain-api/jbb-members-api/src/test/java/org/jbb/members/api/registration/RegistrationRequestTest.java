/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.api.registration;

import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.IPAddress;
import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.DisplayedName;
import org.junit.Test;

public class RegistrationRequestTest {

    @Test
    public void shouldSetAllDataNecessaryToRegister_duringConstruct() throws Exception {
        // given
        Username johnUsername = Username.builder().value("john").build();
        DisplayedName johnName = DisplayedName.builder().value("John").build();
        Email johnEmail = Email.builder().value("john@company.com").build();
        Password password = Password.builder().value("P@ssword1".toCharArray()).build();
        Password passwordAgain = Password.builder().value("P@ssword1".toCharArray()).build();
        IPAddress johnIp = IPAddress.builder().value("127.0.0.1").build();

        // when
        RegistrationRequest request = RegistrationRequest.builder()
            .username(johnUsername)
            .displayedName(johnName)
            .email(johnEmail)
            .password(password)
            .passwordAgain(passwordAgain)
            .ipAddress(johnIp)
            .build();

        // then
        assertThat(request.getUsername()).isEqualTo(johnUsername);
        assertThat(request.getDisplayedName()).isEqualTo(johnName);
        assertThat(request.getEmail()).isEqualTo(johnEmail);
        assertThat(request.getIpAddress()).isEqualTo(johnIp);
        assertThat(request.getPassword()).isEqualTo(password);
        assertThat(request.getPasswordAgain()).isEqualTo(passwordAgain);
    }

}