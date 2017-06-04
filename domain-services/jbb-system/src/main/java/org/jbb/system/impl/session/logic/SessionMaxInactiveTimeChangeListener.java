/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.session.logic;

import org.jbb.lib.mvc.repository.JbbSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

@Component
public class SessionMaxInactiveTimeChangeListener implements PropertyChangeListener {
    private final JbbSessionRepository sessionRepository;

    @Autowired
    public SessionMaxInactiveTimeChangeListener(JbbSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        sessionRepository.setDefaultMaxInactiveInterval(Integer.parseInt(evt.getNewValue().toString()));
    }
}
