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

import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.IPAddress;
import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.DisplayedName;

public interface RegistrationRequest {
    Username getUsername();

    DisplayedName getDisplayedName();

    Email getEmail();

    IPAddress getIPAddress();

    Password getPassword();

    Password getPasswordAgain();

}
