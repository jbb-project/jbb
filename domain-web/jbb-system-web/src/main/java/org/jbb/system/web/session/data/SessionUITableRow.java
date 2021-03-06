/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.session.data;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SessionUITableRow {

    private String id;
    private LocalDateTime creationTime;
    private LocalDateTime lastAccessedTime;
    private Duration usedTime;
    private Duration inactiveTime;
    private String username;
    private String displayedName;
    private Duration timeToLive;
}
