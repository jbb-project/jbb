/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.api.services;

import org.jbb.lib.core.vo.Login;
import org.jbb.security.api.model.Password;

public interface PasswordService {
    void changeFor(Login login, Password newPassword);

    boolean verifyFor(Login login, Password currentPassword);

    boolean verifyExpirationFor(Login login);
}
