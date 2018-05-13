/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.acp.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchLockForm {

    private String displayedName;

    private LockStatus status;

    public enum LockStatus {
        ANY, ACTIVE, INACTIVE
    }

}
