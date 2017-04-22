package org.jbb.system.impl.session;


import org.jbb.system.api.model.session.UserSession;
import org.jbb.system.api.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class SessionServiceImpl implements SessionService{


    @Autowired
    public SessionServiceImpl(){

    }

    @Override
    public List<UserSession> getAllUserSessions() {
        return null;
    }

    @Override
    public void terminateSession(UserSession session) {

    }

    @Override
    public void setDefaultInactiveSessionInterval(Duration maximumInactiveSessionInterval) {

    }

    @Override
    public void getDefaultInactiveSessionInterval(Duration maximumInactiveSessionInterval) {

    }
}


