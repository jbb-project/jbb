/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database;

import org.springframework.stereotype.Component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReconnectionToDbPropertyListener implements PropertyChangeListener {

    private final DatabaseSettingsManager eventSender;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        eventSender.triggerRefresh();
    }
}
