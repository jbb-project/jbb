/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.event;

import org.jbb.lib.core.vo.Username;
import org.jbb.lib.eventbus.JbbEvent;

public class MemberRegistrationEvent extends JbbEvent {
    private final Username username;

    public MemberRegistrationEvent(Username username) {
        this.username = username;
    }

    public Username getUsername() {
        return username;
    }
}
