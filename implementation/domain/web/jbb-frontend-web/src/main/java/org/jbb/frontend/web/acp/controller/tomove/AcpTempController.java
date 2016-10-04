/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.acp.controller.tomove;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller//TODO
public class AcpTempController {
    @RequestMapping("/acp/general")
    public String general() {
        return "acp/general";
    }

    @RequestMapping("/acp/general/board")
    public String generalBoard() {
        return "acp/general/board";
    }

    @RequestMapping("/acp/general/logging")
    public String generalLogging() {
        return "acp/general/logging";
    }

    @RequestMapping("/acp/general/registration")
    public String generalRegistration() {
        return "acp/general/registration";
    }

    @RequestMapping("/acp/members")
    public String members() {
        return "acp/members";
    }

    @RequestMapping("/acp/members/create")
    public String membersCreate() {
        return "acp/members/create";
    }

    @RequestMapping("/acp/members/manage")
    public String membersManage() {
        return "acp/members/manage";
    }

    @RequestMapping("/acp/system")
    public String system() {
        return "acp/system";
    }

    @RequestMapping("/acp/system/database")
    public String systemDatabase() {
        return "acp/system/database";
    }
}
