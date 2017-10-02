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

import com.google.common.eventbus.EventBus;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JbbEventBus extends EventBus {

    private final Validator validator;

    public JbbEventBus(EventExceptionHandler exceptionHandler,
        EventBusAuditRecorder auditRecorder, Validator validator) {
        super(exceptionHandler);
        register(auditRecorder);
        this.validator = validator;
    }

    @Override
    public void post(Object event) {
        if (event instanceof JbbEvent) {
            validateEvent(event);
            super.post(event);
        } else {
            throw new IllegalArgumentException("You should post only JbbEvents through JbbEventBus, not: " + event.getClass());
        }
    }

    private void validateEvent(Object event) {
        Set<ConstraintViolation<Object>> violationSet = validator.validate(event);
        if (!violationSet.isEmpty()) {
            log.warn("Event {} is not valid. Violation set: {}", event, violationSet);
            throw new EventValidationException();
        }
    }
}
