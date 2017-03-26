package org.jbb.system.api.service;


import org.jbb.system.api.model.session.SessionSettings;

import java.util.List;

public interface SessionService {

    List<SessionSettings> getAllUserSessions();

    void terminateSession(SessionSettings sessionSettings);

    void setDefaultInactiveSessionInterval(Long maximumInactiveSessionInterval);

}
