/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database.logic;

import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.system.event.ConnectionToDatabaseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ConnectionToDatabaseEventSender {
    private final JbbEventBus eventBus;

    @Autowired
    public ConnectionToDatabaseEventSender(JbbEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @PostConstruct
    public void emitEvent() {
        eventBus.post(new ConnectionToDatabaseEvent());
    }
}
