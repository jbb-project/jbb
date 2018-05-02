/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.metrics;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MetricPropertyChangeListener implements PropertyChangeListener {
    private final MetricsRegistrar metricsRegistrar;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        metricsRegistrar.update();
    }
}
