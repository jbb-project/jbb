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

import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class EventBusListenersRegistrar {

    private final List<JbbEventBusListener> jbbEventListeners;
    private final JbbEventBus eventBus;

    @PostConstruct
    public void registerAllListeners() {
        jbbEventListeners.forEach(eventBus::register);
    }

}
