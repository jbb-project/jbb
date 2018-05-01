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

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class EventBusListenersRegistrar {

    private final ApplicationContext context;
    private final JbbEventBus eventBus;

    @PostConstruct
    public void registerAllListeners() {
        Collection<JbbEventBusListener> listeners = context
                .getBeansOfType(JbbEventBusListener.class).values();
        listeners.forEach(eventBus::register);
    }

}
