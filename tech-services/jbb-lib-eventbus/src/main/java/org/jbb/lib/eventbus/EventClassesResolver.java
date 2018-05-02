/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.eventbus;

import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class EventClassesResolver {
    private Reflections reflections = new Reflections("org.jbb");

    public Set<Class<? extends JbbEvent>> getEventClasses() {
        return reflections.getSubTypesOf(JbbEvent.class);
    }
}
