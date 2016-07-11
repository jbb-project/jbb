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
import org.jbb.members.api.model.DisplayedName;
import org.jbb.members.api.model.IPAddress;
import org.jbb.members.api.model.Login;
import org.jbb.members.api.model.RegistrationDetails;
import org.jbb.members.api.model.RegistrationInfo;
import org.jbb.members.web.form.RegisterForm;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;


public class RegistrationDetailsImpl implements RegistrationDetails, RegistrationInfo {

    private RegisterForm registerForm;
    private IPAddress iPAddress;
    private LocalDateTime localDateTime;

    public RegistrationDetailsImpl(RegisterForm info, HttpServletRequest httpServletRequest) {
        this.registerForm = info;
        this.iPAddress = IPAddress.builder().ipAddress(httpServletRequest.getRemoteAddr()).build();
        this.localDateTime = LocalDateTime.now();

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
    public LocalDateTime getRegistrationDate() {
        return localDateTime;
    }

    @Override
    public IPAddress getIPAddress() {
        return iPAddress;
    }
}
