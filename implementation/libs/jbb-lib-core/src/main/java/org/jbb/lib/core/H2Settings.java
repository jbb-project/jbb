/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.core;

import org.springframework.stereotype.Component;
import org.springframework.util.SocketUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class H2Settings {
    @Getter
    @Setter
    private Mode mode;

    private Integer port;

    public H2Settings() {
        this.mode = Mode.SERVER;
    }

    public Integer getPort() {
        if (port == null) {
            port = SocketUtils.findAvailableTcpPort();
            log.info("Port {} has been chosen for H2 database server", port);
        }
        return port;
    }

    public enum Mode {
        SERVER, FILE
    }
}
