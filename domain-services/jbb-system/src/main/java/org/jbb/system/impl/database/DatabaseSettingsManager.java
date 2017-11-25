/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database;

import java.beans.PropertyChangeEvent;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.db.DbPropertyChangeListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSettingsManager {

    private final DbPropertyChangeListener dbPropertyChangeListener;

    public void triggerRefresh() {
        dbPropertyChangeListener.propertyChange(new PropertyChangeEvent(this, "db", null, null));
    }
}
