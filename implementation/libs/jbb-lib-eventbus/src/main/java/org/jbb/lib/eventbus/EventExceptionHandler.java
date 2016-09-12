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

import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
class EventExceptionHandler implements SubscriberExceptionHandler {
    @Override
    public void handleException(Throwable exception, SubscriberExceptionContext context) {
        //IDEA Depends of exception type make some retry policies
        log.error("Error during event [{}] consuming", context.getEvent(), exception);
    }
}
