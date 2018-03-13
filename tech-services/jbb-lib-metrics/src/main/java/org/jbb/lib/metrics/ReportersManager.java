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

import java.util.Arrays;

public abstract class ReportersManager {

    abstract void init(MetricProperties properties, MetricType type);

    abstract void configure(MetricProperties properties, MetricType type);

    public void configure(MetricProperties properties) {
        Arrays.stream(MetricType.values()).forEach(type -> configure(properties, type));
    }

    public void init(MetricProperties properties) {
        Arrays.stream(MetricType.values()).forEach(type -> init(properties, type));
    }

}
