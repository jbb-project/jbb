/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.event;

import org.apache.commons.lang3.Validate;
import org.jbb.lib.core.vo.Username;
import org.jbb.lib.eventbus.JbbEvent;

import java.util.Optional;

public class SignInFailedEvent extends JbbEvent {
    private final Long memberId;
    private final Username username;

    public SignInFailedEvent(Long memberId, Username username) {
        Validate.notNull(username);

        this.memberId = memberId;
        this.username = username;
    }

    public Username getUsername() {
        return username;
    }

    public Optional<Long> getMemberId() {
        return Optional.ofNullable(memberId);
    }
}
