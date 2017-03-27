package org.jbb.system.api.service;


import org.jbb.system.api.model.session.UserSession;

import java.time.Duration;
import java.util.List;

public interface SessionService {

    List<UserSession> getAllUserSessions();

    void terminateSession(UserSession session);

    void setDefaultInactiveSessionInterval(Duration maximumInactiveSessionInterval);

    void getDefaultInactiveSessionInterval(Duration maximumInactiveSessionInterval);

}
