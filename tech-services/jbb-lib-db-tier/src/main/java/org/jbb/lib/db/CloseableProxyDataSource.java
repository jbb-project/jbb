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

import com.zaxxer.hikari.HikariDataSource;

import net.ttddyy.dsproxy.support.ProxyDataSource;

import java.sql.SQLException;

public class CloseableProxyDataSource extends ProxyDataSource {

    public CloseableProxyDataSource(LoggingProxyDataSource loggingProxyDataSource) {
        super(loggingProxyDataSource);
    }

    @Override
    public void close() {
        try {
            super.unwrap(HikariDataSource.class).close();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
