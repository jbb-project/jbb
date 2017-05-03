package org.jbb.system.web.session;

import org.jbb.system.api.model.session.UserSession;
import org.jbb.system.api.service.SessionService;
import org.jbb.system.web.session.data.SessionRow;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.stream.Collectors;

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
        List<SessionRow> sessionRowList = userSessionList.stream()
                .map(userSession ->
                        new SessionRow(userSession.sessionId(),
                                        userSession.creationTime(),
                                        userSession.lastAccessedTime(),
                                        userSession.usedTime(),
                                        userSession.inactiveTime(),
                                        userSession.userName(),
                                        userSession.displayUserName(),
                                        userSession.timeToLive()))
                .collect(Collectors.toList());
        model.addAttribute("userSessions",sessionRowList);

        return VIEW_NAME;
    }




}
