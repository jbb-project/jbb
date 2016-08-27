/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.events;

import org.jbb.lib.core.vo.Login;
import org.jbb.lib.eventbus.JbbEvent;

public class PasswordChangedEvent extends JbbEvent {
    private final Login login;

    public PasswordChangedEvent(Login login) {
        this.login = login;
    }

    public Login getLogin() {
        return login;
    }
}
