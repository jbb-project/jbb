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
import java.time.LocalDateTime;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.jbb.lib.commons.RequestIdUtils;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.commons.web.HttpServletRequestHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JbbEventBus extends EventBus {

    private final Validator validator;
    private final UserDetailsSource userDetailsSource;
    private final HttpServletRequestHolder servletRequestHolder;

    public JbbEventBus(EventExceptionHandler exceptionHandler,
        EventBusAuditRecorder auditRecorder, Validator validator) {
        super(exceptionHandler);
        register(auditRecorder);
        this.validator = validator;
        this.userDetailsSource = new UserDetailsSource();
        this.servletRequestHolder = new HttpServletRequestHolder();
    }

    @Override
    public void post(Object event) {
        if (event instanceof JbbEvent) {
            JbbEvent jbbEvent = (JbbEvent) event;
            includeMetaData(jbbEvent);
            validateEvent(jbbEvent);
            jbbEvent.setPublishDateTime(LocalDateTime.now());
            super.post(event);
        } else {
            throw new IllegalArgumentException("You should post only JbbEvents through JbbEventBus, not: " + event.getClass());
        }
    }

    private void includeMetaData(JbbEvent event) {
        event.setSourceRequestId(RequestIdUtils.getCurrentRequestId());

        SecurityContentUser securityContentUser = userDetailsSource.getFromApplicationContext();
        if (securityContentUser != null) {
            event.setSourceMemberId(securityContentUser.getUserId());
        }

        HttpServletRequest currentHttpRequest = servletRequestHolder.getCurrentHttpRequest();
        if (currentHttpRequest != null) {
            event.setSourceIpAddress(currentHttpRequest.getRemoteAddr());

            HttpSession session = currentHttpRequest.getSession();
            if (session != null) {
                event.setSourceSessionId(session.getId());
            }
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
