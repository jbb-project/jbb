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
import org.jbb.lib.core.vo.Login;
import org.jbb.lib.core.vo.Password;
import org.jbb.members.api.model.DisplayedName;
import org.jbb.members.api.model.RegistrationRequest;
import org.jbb.members.web.form.RegisterForm;


public class RegistrationRequestImpl implements RegistrationRequest {

    private RegisterForm registerForm;
    private IPAddress iPAddress;

    public RegistrationRequestImpl(RegisterForm form, IPAddress ipAddress) {
        this.registerForm = form;
        this.iPAddress = ipAddress;
    }

    @Override
    public Login getLogin() {
        return Login.builder().value(registerForm.getLogin()).build();
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
