/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import lombok.extern.slf4j.Slf4j;

import static org.jbb.lib.commons.RequestIdUtils.cleanRequestId;
import static org.jbb.lib.commons.RequestIdUtils.generateNewRequestId;
import static org.jbb.lib.commons.RequestIdUtils.getCurrentRequestId;

@Slf4j
public class RequestIdListener implements ServletRequestListener {

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        String newRequestId = generateNewRequestId();
        log.debug("Processing of request {} started", newRequestId);
    }

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        log.debug("Processing of request {} finished", getCurrentRequestId());
        cleanRequestId();
    }

}
