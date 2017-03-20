/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.db;

import org.h2.tools.Server;
import org.jbb.lib.core.H2Settings;
import org.springframework.beans.factory.InitializingBean;

import java.sql.SQLException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmbeddedDatabaseServerManager implements InitializingBean {
    private H2Settings h2Settings;

    public EmbeddedDatabaseServerManager(H2Settings h2Settings) {
        this.h2Settings = h2Settings;
        Runtime.getRuntime().addShutdownHook(new Thread(this::stopH2Server));
    }

    public void startH2Server() {
        try {
            if (h2Settings.getMode() == H2Settings.Mode.SERVER) {
                Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public void stopH2Server() {
        try {
            if (h2Settings.getMode() == H2Settings.Mode.SERVER) {
                Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
            }
        } catch (SQLException e) {//NOSONAR
            log.trace("H2 Server shutdown error", e);
            // ignore...
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        startH2Server();
    }
}
