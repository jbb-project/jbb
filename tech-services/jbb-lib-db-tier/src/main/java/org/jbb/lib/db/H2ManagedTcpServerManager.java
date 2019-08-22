/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.db;

import org.apache.commons.lang3.StringUtils;
import org.h2.tools.Server;
import org.jbb.lib.db.provider.H2ManagedServerProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.SocketUtils;

import java.sql.SQLException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class H2ManagedTcpServerManager implements InitializingBean {

    private final DbProperties dbProperties;
    private final H2ManagedServerProvider h2ManagedServerProvider;

    public H2ManagedTcpServerManager(DbProperties dbProperties,
                                     H2ManagedServerProvider h2ManagedServerProvider) {
        this.dbProperties = dbProperties;
        this.h2ManagedServerProvider = h2ManagedServerProvider;
        Runtime.getRuntime().addShutdownHook(new Thread(this::stopH2Server));
    }

    public void startH2Server() {
        try {
            if (h2ManagedServerProvider.isCurrentProvider()) {
                Server.createTcpServer("-tcpPort", dbProperties.h2ManagedServerDbPort().toString(),
                        "-tcpAllowOthers", "-ifNotExists").start();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public void stopH2Server() {
        try {
            if (h2ManagedServerProvider.isCurrentProvider()) {
                Server.shutdownTcpServer("tcp://localhost:" + dbProperties.h2ManagedServerDbPort(),
                        "", true, true);
            }
        } catch (SQLException e) {//NOSONAR
            log.trace("H2 Server shutdown error", e);
            // ignore...
        }
    }

    @Override
    public void afterPropertiesSet() {
        resolveH2Port();
        startH2Server();
    }

    private void resolveH2Port() {
        if (!dbProperties.propertyNames().contains(DbProperties.H2_MANAGED_SERVER_DB_PORT_KEY)
                || StringUtils
                .isBlank(dbProperties.getProperty(DbProperties.H2_MANAGED_SERVER_DB_PORT_KEY))) {
            Integer randomPort = SocketUtils.findAvailableTcpPort();
            dbProperties.setProperty(DbProperties.H2_MANAGED_SERVER_DB_PORT_KEY, randomPort.toString());
            log.info("Port {} has been chosen for H2 database managed server", randomPort);
        }
    }
}
