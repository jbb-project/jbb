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

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Component
@Order(0)
public class ApiNullModelViewStrategy extends ReplacingViewStrategy {

    @Override
    boolean canHandle(ModelAndView modelAndView) {
        return modelAndView == null;
    }

    @Override
    void performHandle(ModelAndView modelAndView) {
        // do nothing...
    }
}
