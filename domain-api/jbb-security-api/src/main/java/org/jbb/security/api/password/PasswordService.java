/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.api.password;

import java.util.Optional;
import org.jbb.lib.commons.vo.Password;

public interface PasswordService {
    void changeFor(Long memberId, Password newPassword);

    boolean verifyFor(Long memberId, Password currentPassword);

    Optional<String> getPasswordHash(Long memberId);

    PasswordPolicy currentPolicy();

    void updatePolicy(PasswordPolicy policy);
}
