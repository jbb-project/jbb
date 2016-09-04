/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.event;


import com.google.common.base.MoreObjects;

import org.jbb.lib.eventbus.JbbEvent;

public class SwitchPageEvent extends JbbEvent {
    private final String viewName;

    public SwitchPageEvent(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("viewName", viewName)
                .toString();
    }
}
