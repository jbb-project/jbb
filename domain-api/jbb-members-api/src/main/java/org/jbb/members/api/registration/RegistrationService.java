/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.api.registration;

public interface RegistrationService {

    void register(RegistrationRequest request);

    boolean isEmailDuplicationAllowed();

    void allowEmailDuplication(boolean allow);

    RegistrationMetaData getRegistrationMetaData(Long memberId);
}
