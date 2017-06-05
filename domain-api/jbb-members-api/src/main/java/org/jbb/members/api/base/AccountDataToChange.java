/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.api.base;

import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Password;

import java.util.Optional;

public interface AccountDataToChange {
    Optional<Email> getEmail();

    Optional<Password> getNewPassword();
}
