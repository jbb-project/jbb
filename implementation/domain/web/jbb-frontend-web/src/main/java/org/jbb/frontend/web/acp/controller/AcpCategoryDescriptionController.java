/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.acp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AcpCategoryDescriptionController {
    @RequestMapping("/acp/general")
    public String general() {
        return "acp/general"; //NOSONAR
    }

    @RequestMapping("/acp/members")
    public String members() {
        return "acp/members"; //NOSONAR
    }

    @RequestMapping("/acp/system")
    public String system() {
        return "acp/system"; //NOSONAR
    }
}
