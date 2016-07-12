/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.model;

import org.jbb.members.api.model.DisplayedName;
import org.jbb.members.api.model.Login;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberBrowserRow {
    private Login login;
    private DisplayedName displayedName;
    private LocalDateTime registrationDate;
}
