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
import org.jbb.lib.eventbus.JbbEvent;

import lombok.Getter;
import lombok.ToString;

@ToString
public class AdministratorRoleRemovedEvent extends JbbEvent {
    @Getter
    private final Long memberId;

    public AdministratorRoleRemovedEvent(Long memberId) {
        Validate.notNull(memberId);
        this.memberId = memberId;
    }

}
