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

import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.h2.tools.Server;
import org.jbb.lib.commons.H2Settings;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.SocketUtils;

@Slf4j
public class EmbeddedDatabaseServerManager implements InitializingBean {

    private final DbProperties dbProperties;
    private final H2Settings h2Settings;

    public EmbeddedDatabaseServerManager(DbProperties dbProperties, H2Settings h2Settings) {
        this.dbProperties = dbProperties;
        this.h2Settings = h2Settings;
        Runtime.getRuntime().addShutdownHook(new Thread(this::stopH2Server));
    }

    public void startH2Server() {
        try {
            if (h2Settings.getMode() == H2Settings.Mode.SERVER) {
                Server.createTcpServer("-tcpPort", h2Settings.getPort().toString(), "-tcpAllowOthers").start();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public void stopH2Server() {
        try {
            if (h2Settings.getMode() == H2Settings.Mode.SERVER) {
                Server.shutdownTcpServer("tcp://localhost:" + h2Settings.getPort(), "", true, true);
            }
        } catch (SQLException e) {//NOSONAR
            log.trace("H2 Server shutdown error", e);
            // ignore...
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        resolveH2Port();
        startH2Server();
    }

    private Integer resolveH2Port() {
        if (dbProperties.propertyNames().contains(DbProperties.H2_MANAGED_SERVER_DB_PORT_KEY)
                && StringUtils.isNotBlank(dbProperties.getProperty(DbProperties.H2_MANAGED_SERVER_DB_PORT_KEY))) {
            Integer propertiesPort = dbProperties.h2ManagedServerDbPort();
            h2Settings.setPort(propertiesPort);
        } else {
            Integer randomPort = SocketUtils.findAvailableTcpPort();
            dbProperties.setProperty(DbProperties.H2_MANAGED_SERVER_DB_PORT_KEY, randomPort.toString());
            h2Settings.setPort(randomPort);
            log.info("Port {} has been chosen for H2 database server", randomPort);
        }

        return h2Settings.getPort();
    }
}
