/*
 * Copyright (C) 2020 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.impl.webhooks.event;

import com.google.common.collect.Lists;

import com.github.zafarkhaja.semver.Version;

import org.jbb.integration.api.webhooks.event.EventType;
import org.jbb.lib.eventbus.EventClassesResolver;
import org.jbb.lib.eventbus.JbbEvent;
import org.jbb.lib.eventbus.webhooks.WebhookEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventTypesResolver {

    public final EventClassesResolver eventClassesResolver;

    // FIXME caching!
    public List<EventType> getEventTypes() {
        Set<Class<? extends JbbEvent>> eventClasses = eventClassesResolver.getEventClasses();
        return eventClasses.stream()
                .flatMap(eventClass -> findEventTypes(eventClass).stream())
                .sorted(Comparator.comparing(a -> Version.valueOf(a.getVersion() + ".0")))
                .sorted(Comparator.comparing(EventType::getName))
                .collect(Collectors.toList());
    }

    private List<EventType> findEventTypes(Class<? extends JbbEvent> eventClass) {
        WebhookEvent webhookAnnotation = eventClass.getAnnotation(WebhookEvent.class);
        return Optional.ofNullable(webhookAnnotation)
                .map(a -> Lists.newArrayList(a.versions()))
                .orElse(new ArrayList<>())
                .stream()
                .map(version -> new EventType(webhookAnnotation.name(), version))
                .collect(Collectors.toList());
    }
}
