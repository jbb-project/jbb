/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.services;

import org.jbb.lib.core.vo.Email;
import org.jbb.lib.core.vo.IPAddress;
import org.jbb.members.api.model.DisplayedName;
import org.jbb.members.api.model.Login;
import org.jbb.members.api.model.RegistrationRequest;

public class RegistrationRequestImpl implements RegistrationRequest {
    private String login;
    private String displayedName;
    private String email;
    private String ipAddress;

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public Login getLogin() {
        return Login.builder().value(login).build();
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public DisplayedName getDisplayedName() {
        return DisplayedName.builder().value(displayedName).build();
    }

    public void setDisplayedName(String displayedName) {
        this.displayedName = displayedName;
    }

    @Override
    public Email getEmail() {
        return Email.builder().value(email).build();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public IPAddress getIPAddress() {
        return IPAddress.builder().value(ipAddress).build();
    }
}
