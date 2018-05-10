/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.logging;

import java.util.List;
import java.util.function.Predicate;
import javax.xml.bind.JAXBElement;
import org.jbb.lib.logging.health.NopMonitoredLogbackStatusListener;
import org.jbb.lib.logging.health.OnConsoleMonitoredLogbackStatusListener;
import org.jbb.lib.logging.jaxb.Configuration;
import org.jbb.lib.logging.jaxb.StatusListener;
import org.springframework.stereotype.Component;

@Component
public class StatusListenerEditor {

    public static final String NO_OP_LISTENER = NopMonitoredLogbackStatusListener.class
        .getCanonicalName();
    public static final String CONSOLE_LISTENER = OnConsoleMonitoredLogbackStatusListener.class
        .getCanonicalName();

    public void setAppropriateStatusListener(Configuration configuration, boolean enableDebug) {
        configuration.setDebug(enableDebug);

        List<Object> confElements = configuration
            .getShutdownHookOrStatusListenerOrContextListener();
        Object statusListener = confElements.stream()
            .filter(statusListener())
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No status listener defined in config"));
        StatusListener listener = (StatusListener) ((JAXBElement) statusListener).getValue();
        listener.setClazz(resolveClassName(enableDebug));
    }

    private String resolveClassName(boolean enableDebug) {
        return enableDebug ? CONSOLE_LISTENER : NO_OP_LISTENER;
    }

    private Predicate<? super Object> statusListener() {
        return o -> o instanceof JAXBElement && ((JAXBElement) o).getDeclaredType()
            .equals(StatusListener.class);
    }

}
