package org.jbb.system.web.session;

import org.jbb.system.api.service.SessionService;
import org.springframework.stereotype.Controller;

@Controller
public class SessionController {

    private SessionService sessionService;

    public SessionController(SessionService sessionService){
        this.sessionService = sessionService;
    }
}
