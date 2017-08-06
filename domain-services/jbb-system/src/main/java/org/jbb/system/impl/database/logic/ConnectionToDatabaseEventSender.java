/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database.logic;

import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.system.event.DatabaseSettingsChangedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConnectionToDatabaseEventSender {
    private final JbbEventBus eventBus;

    @PostConstruct
    public void emitEvent() {
        eventBus.post(new DatabaseSettingsChangedEvent());
    }
}
