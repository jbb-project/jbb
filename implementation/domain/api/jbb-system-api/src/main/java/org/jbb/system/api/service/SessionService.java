package org.jbb.system.api.service;


import org.jbb.system.api.model.session.UserSession;

import java.time.Duration;
import java.util.List;

public interface SessionService {

    List<UserSession> getAllUserSessions();

    void terminateSession(String sessionId);

    void setDefaultInactiveSessionInterval(Duration maximumInactiveSessionInterval);

    Duration getDefaultInactiveSessionInterval();

}
