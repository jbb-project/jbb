package org.jbb.system.impl.session;


import com.google.common.collect.Lists;

import org.jbb.lib.mvc.repository.JbbSessionRepository;
import org.jbb.system.api.model.session.UserSession;
import org.jbb.system.api.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.ExpiringSession;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class SessionServiceImpl implements SessionService{


    private final JbbSessionRepository jbbSessionRepository;

    @Autowired
    public SessionServiceImpl(JbbSessionRepository jbbSessionRepository){
        this.jbbSessionRepository=jbbSessionRepository;
    }

    @Override
    public List<UserSession> getAllUserSessions()
    {
        Map<String, ExpiringSession> jbbSessionRepositorySessionMap = jbbSessionRepository.getSessionMap();

        return Lists.newArrayList();
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


