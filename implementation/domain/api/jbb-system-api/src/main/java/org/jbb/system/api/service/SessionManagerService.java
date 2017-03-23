package org.jbb.system.api.service;


import org.jbb.system.api.model.session.SessionSettings;

import java.util.stream.Stream;

public interface SessionManagerService {

    Stream<SessionSettings> getAllUserSessions();

    void terminateSession(SessionSettings sessionSettings);

    void setMaximumInactiveSessionInterval(Long maximumInactiveSessionInterval);

}
