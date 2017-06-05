/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.registration.data;

import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.IPAddress;
import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.web.registration.form.RegisterForm;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationRequestImplTest {
    @Test
    public void shouldSetAllDataNecessaryToRegister_duringConstruct() throws Exception {
        // given
        RegisterForm form = new RegisterForm();
        form.setUsername("john");
        form.setDisplayedName("John");
        form.setEmail("john@company.com");
        form.setPassword("P@ssword1");
        form.setPasswordAgain("P@ssword1");

        IPAddress ipAddress = IPAddress.builder().value("127.0.0.1").build();

        Username johnUsername = Username.builder().value("john").build();
        DisplayedName johnName = DisplayedName.builder().value("John").build();
        Email johnEmail = Email.builder().value("john@company.com").build();
        IPAddress johnIp = IPAddress.builder().value("127.0.0.1").build();
        Password password = Password.builder().value("P@ssword1".toCharArray()).build();
        Password passwordAgain = Password.builder().value("P@ssword1".toCharArray()).build();

        // when
        RegistrationRequestImpl request = new RegistrationRequestImpl(form, ipAddress);

        // then
        assertThat(request.getUsername()).isEqualTo(johnUsername);
        assertThat(request.getDisplayedName()).isEqualTo(johnName);
        assertThat(request.getEmail()).isEqualTo(johnEmail);
        assertThat(request.getIPAddress()).isEqualTo(johnIp);
        assertThat(request.getPassword()).isEqualTo(password);
        assertThat(request.getPasswordAgain()).isEqualTo(passwordAgain);
    }
}