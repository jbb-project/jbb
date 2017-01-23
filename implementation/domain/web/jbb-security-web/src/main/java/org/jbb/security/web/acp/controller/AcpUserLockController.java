/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.acp.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@RequestMapping("/acp/general/lock")
public class AcpUserLockController {

    @RequestMapping(method = RequestMethod.GET)
    public String userLockSettingsPanelGet() {
        return "acp/general/lock";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String userLockSettingsPanelPost() {
        return "acp/general/lock";
    }
}
