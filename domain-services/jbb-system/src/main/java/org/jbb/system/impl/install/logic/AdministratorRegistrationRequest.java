/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.install.logic;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.IPAddress;
import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.registration.RegistrationRequest;
import org.jbb.system.api.install.InstallationData;


public class AdministratorRegistrationRequest implements RegistrationRequest {

    private Username adminUsername;
    private DisplayedName adminDisplayedName;
    private Email adminEmail;
    private Password adminPassword;

    public AdministratorRegistrationRequest(InstallationData installationData) {
        this.adminUsername = Username.builder().value(installationData.getAdminUsername()).build();
        this.adminDisplayedName = DisplayedName.builder()
            .value(installationData.getAdminDisplayedName()).build();
        this.adminEmail = Email.builder().value(installationData.getAdminEmail()).build();
        this.adminPassword = Password.builder()
            .value(installationData.getAdminPassword().toCharArray()).build();
    }

    @Override
    public Username getUsername() {
        return adminUsername;
    }

    @Override
    public DisplayedName getDisplayedName() {
        return adminDisplayedName;
    }

    @Override
    public Email getEmail() {
        return adminEmail;
    }

    @Override
    public IPAddress getIPAddress() {
        try {
            return IPAddress.builder().value(Inet4Address.getLocalHost().getHostAddress()).build();
        } catch (UnknownHostException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Password getPassword() {
        return adminPassword;
    }

    @Override
    public Password getPasswordAgain() {
        return adminPassword;
    }
}
