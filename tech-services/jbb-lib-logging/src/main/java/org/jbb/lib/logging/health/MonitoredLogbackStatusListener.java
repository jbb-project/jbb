/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.logging.health;

import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusListener;

public abstract class MonitoredLogbackStatusListener implements StatusListener {

    public final StatusListener delegateStatusListener;

    protected MonitoredLogbackStatusListener(StatusListener delegateStatusListener) {
        this.delegateStatusListener = delegateStatusListener;
    }

    @Override
    public void addStatusEvent(Status status) {
        LogbackStateStorage.putStatus(status);
        delegateStatusListener.addStatusEvent(status);
    }

}
