/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.logic.view;

import org.springframework.web.servlet.ModelAndView;

public abstract class ReplacingViewStrategy {
    public static final String DEFAULT_LAYOUT_NAME = "defaultLayout";
    public static final String INSTALL_LAYOUT_NAME = "installLayout";
    public static final String CONTENT_VIEW_NAME = "contentViewName";

    abstract boolean canHandle(ModelAndView modelAndView);

    public boolean handle(ModelAndView modelAndView) {
        if (canHandle(modelAndView)) {
            performHandle(modelAndView);
            return true;
        }
        return false;
    }

    abstract void performHandle(ModelAndView modelAndView);


}
