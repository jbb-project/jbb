/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.services;

import org.jbb.lib.core.vo.Login;
import org.jbb.security.api.model.Password;
import org.jbb.security.api.services.PasswordService;
import org.springframework.stereotype.Service;

@Service //TODO
public class PasswordServiceImpl implements PasswordService {
    @Override
    public void changeFor(Login login, Password newPassword) {

    }

    @Override
    public boolean verifyFor(Login login, Password currentPassword) {
        return false;
    }

    @Override
    public boolean verifyExpirationFor(Login login) {
        return false;
    }
}
