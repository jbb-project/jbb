/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.model.session;


import java.time.Duration;
import java.time.LocalDateTime;


public interface UserSession {

    String sessionId();

    LocalDateTime creationTime();

    LocalDateTime lastAccessedTime();

    Duration usedTime();

    Duration inactiveTime();

    Duration timeToLive();

    String userName();

    String displayUserName();
}
