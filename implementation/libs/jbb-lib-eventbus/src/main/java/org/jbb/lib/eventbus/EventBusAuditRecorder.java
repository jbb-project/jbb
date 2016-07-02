/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.eventbus;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class EventBusAuditRecorder {
    @Subscribe
    public void logJbbEvent(JbbEvent jbbEvent) {
        log.info("Retreving event with UUID: {} of class {}", jbbEvent.getUuid(), jbbEvent.getClass().getName());
    }

    @Subscribe
    public void logDeadEvent(DeadEvent deadEvent) {
        log.warn("Event [{}] sourced by {} has not been consumed by any listener",
                deadEvent.getEvent(), deadEvent.getSource());
    }
}
