/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.model;

import org.jbb.lib.core.vo.Email;
import org.jbb.lib.core.vo.IPAddress;
import org.jbb.members.api.model.DisplayedName;
import org.jbb.members.api.model.Login;
import org.jbb.members.web.form.RegisterForm;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationRequestImplTest {
    @Test
    public void shouldSetAllDataNecessaryToRegister_duringConstruct() throws Exception {
        // given
        RegisterForm form = new RegisterForm();
        form.setLogin("john");
        form.setDisplayedName("John");
        form.setEmail("john@company.com");

        IPAddress ipAddress = IPAddress.builder().value("127.0.0.1").build();

        Login johnLogin = Login.builder().value("john").build();
        DisplayedName johnName = DisplayedName.builder().value("John").build();
        Email johnEmail = Email.builder().value("john@company.com").build();
        IPAddress johnIp = IPAddress.builder().value("127.0.0.1").build();

        // when
        RegistrationRequestImpl request = new RegistrationRequestImpl(form, ipAddress);

        // then
        assertThat(request.getLogin()).isEqualTo(johnLogin);
        assertThat(request.getDisplayedName()).isEqualTo(johnName);
        assertThat(request.getEmail()).isEqualTo(johnEmail);
        assertThat(request.getIPAddress()).isEqualTo(johnIp);
    }
}