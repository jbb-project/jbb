/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.session;


import java.time.Duration;
import java.util.List;
import java.util.Optional;

public interface SessionService {

    List<MemberSession> getAllUserSessions();

    Optional<MemberSession> getSession(String sessionId);

    MemberSession getSessionChecked(String sessionId) throws SessionNotFoundException;

    void terminateSession(String sessionId);

    Duration getMaxInactiveSessionInterval();

    void setMaxInactiveSessionInterval(Duration maximumInactiveSessionInterval);

}
