/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.api.service;

import org.jbb.lib.core.vo.Password;
import org.jbb.lib.core.vo.Username;
import org.jbb.security.api.data.PasswordRequirements;

public interface PasswordService {
    void changeFor(Username username, Password newPassword);

    boolean verifyFor(Username username, Password currentPassword);

    PasswordRequirements currentRequirements();

    void updateRequirements(PasswordRequirements requirements);
}
