/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.impl.broker;

import org.apache.qpid.server.SystemLauncher;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

// https://cwiki.apache.org/confluence/display/qpid/How+to+embed+Qpid+Broker-J
public class EmbeddedBroker extends SystemLauncher {
    private static final String INITIAL_CONFIGURATION = "qpid-initial-config.json";

    @PostConstruct
    public void startUp() throws Exception {
        startup(createSystemConfig());
    }

    private Map<String, Object> createSystemConfig() {
        Map<String, Object> attributes = new HashMap<>();
        URL initialConfig = EmbeddedBroker.class.getClassLoader().getResource(INITIAL_CONFIGURATION);
        attributes.put("type", "Memory");
        attributes.put("initialConfigurationLocation", initialConfig.toExternalForm());
        attributes.put("startupLoggedToSystemOut", true);
        return attributes;
    }
}
