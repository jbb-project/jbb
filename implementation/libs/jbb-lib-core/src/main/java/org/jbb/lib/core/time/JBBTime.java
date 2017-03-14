/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.core.time;


import java.time.Clock;
import java.time.LocalDateTime;

public class JBBTime {

    private static Clock clock = Clock.systemDefaultZone();

    public static LocalDateTime now() {
        return LocalDateTime.now(clock);
    }

    public static void setClock(Clock clock) {
        JBBTime.clock = clock;
    }
}
