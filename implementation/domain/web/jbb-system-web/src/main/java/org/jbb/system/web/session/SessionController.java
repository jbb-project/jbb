package org.jbb.system.web.session;

import org.jbb.system.api.model.session.UserSession;
import org.jbb.system.api.service.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/acp/system/sessions")
public class SessionController {

    private SessionService sessionService;
    private static final String VIEW_NAME = "acp/system/sessions";


    public SessionController(SessionService sessionService){
        this.sessionService = sessionService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getSessionView(Model model){

        List<UserSession> userSessionList = sessionService.getAllUserSessions();
        model.addAttribute("userSessions",userSessionList);

        return VIEW_NAME;
    }




}
