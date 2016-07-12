/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.api.services;

import org.jbb.members.api.exceptions.RegistrationException;
import org.jbb.members.api.model.Member;
import org.jbb.members.api.model.RegistrationDetails;
import org.jbb.members.api.model.RegistrationInfo;

import java.util.Optional;

public interface RegistrationService {
    void register(RegistrationDetails details) throws RegistrationException;

    Optional<RegistrationInfo> getRegistrationInfo(Member member);
}
