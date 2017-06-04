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
import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.api.data.RegistrationRequest;
import org.jbb.members.web.registration.form.RegisterForm;


public class RegistrationRequestImpl implements RegistrationRequest {

    private RegisterForm registerForm;
    private IPAddress iPAddress;

    public RegistrationRequestImpl(RegisterForm form, IPAddress ipAddress) {
        this.registerForm = form;
        this.iPAddress = ipAddress;
    }

    @Override
    public Username getUsername() {
        return Username.builder().value(registerForm.getUsername()).build();
    }

    @Override
    public DisplayedName getDisplayedName() {
        return DisplayedName.builder().value(registerForm.getDisplayedName()).build();
    }

    @Override
    public Email getEmail() {
        return Email.builder().value(registerForm.getEmail()).build();
    }

    @Override
    public IPAddress getIPAddress() {
        return iPAddress;
    }

    @Override
    public Password getPassword() {
        return Password.builder().value(registerForm.getPassword().toCharArray()).build();
    }

    @Override
    public Password getPasswordAgain() {
        return Password.builder().value(registerForm.getPasswordAgain().toCharArray()).build();
    }
}
