/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.eventbus;

import com.google.common.eventbus.Subscribe;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
class EventBusAuditRecorder {
    @Subscribe
    public void logJbbEvent(JbbEvent jbbEvent) {
        log.info("Publish event with UUID: {} of class {}. Details: {}", jbbEvent.getUuid(), jbbEvent.getClass().getName(), jbbEvent);
    }
}
