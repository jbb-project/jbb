/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.logic.view;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Component
@Order(5)
public class DefaultReplacingViewStrategy extends ReplacingViewStrategy {
    @Override
    boolean canHandle(ModelAndView modelAndView) {
        return true;
    }

    @Override
    void performHandle(ModelAndView modelAndView) {
        modelAndView.getModel().put(CONTENT_VIEW_NAME, modelAndView.getViewName());
        if (modelAndView.getViewName().equals("install") || modelAndView.getViewName()
            .equals("health")) {
            modelAndView.setViewName(NO_NAVBAR_LAYOUT_NAME);
        } else {
            modelAndView.setViewName(DEFAULT_LAYOUT_NAME);
        }
    }
}
