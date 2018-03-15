/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.eventbus.metrics;

import org.jbb.lib.eventbus.EventClassesResolver;
import org.jbb.lib.eventbus.JbbEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JbbEventMetrics implements MeterBinder {

    private final EventClassesResolver eventClassesResolver;
    private final Iterable<Tag> tags;

    private MeterRegistry registry;
    private Map<String, Counter> counterMap;

    @Override
    public void bindTo(MeterRegistry registry) {
        this.registry = registry;
        createCounters();
    }

    public void incrementMetricCounter(JbbEvent jbbEvent) {
        String eventName = jbbEvent.getClass().getSimpleName();
        Counter counter = counterMap.get(eventName);
        if (counter != null) {
            counter.increment();
        }
    }

    private void createCounters() {
        counterMap = new HashMap<>();
        List<String> eventNames = eventClassesResolver.getEventClasses().stream()
                .map(Class::getSimpleName)
                .collect(Collectors.toList());

        eventNames.forEach(this::createCounter);
    }

    private void createCounter(String eventName) {
        Counter counter = Counter.builder("event")
                .tags(tags).tags("type", eventName)
                .description("Number of jBB events of type '" + eventName + "'")
                .register(registry);
        counterMap.put(eventName, counter);
    }
}
