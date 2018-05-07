/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.health.checks;

import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;
import org.jbb.lib.health.JbbHealthCheck;
import org.springframework.stereotype.Component;

@Component
public class DeadlockHealthCheck extends JbbHealthCheck {

    private final ThreadDeadlockHealthCheck threadDeadlockHealthCheck = new ThreadDeadlockHealthCheck();

    @Override
    public String getName() {
        return "Thread deadlocks";
    }

    @Override
    protected Result check() throws Exception {
        return threadDeadlockHealthCheck.execute();
    }
}
