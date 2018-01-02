/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.sync;

import org.springframework.stereotype.Component;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PermissionSyncManager {

    private final List<SyncHandler> syncHandlers;

    public void sync() {
        syncHandlers.forEach(SyncHandler::synchronize);
    }
}
