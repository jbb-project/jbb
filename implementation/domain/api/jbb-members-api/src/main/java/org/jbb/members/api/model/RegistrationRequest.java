/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.api.model;

import org.jbb.lib.core.vo.Email;
import org.jbb.lib.core.vo.IPAddress;

public interface RegistrationRequest {
    Login getLogin();

    DisplayedName getDisplayedName();

    Email getEmail();

    IPAddress getIPAddress();

}