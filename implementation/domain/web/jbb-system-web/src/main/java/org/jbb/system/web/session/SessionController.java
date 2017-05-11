package org.jbb.system.web.session;

import org.apache.commons.lang3.Validate;
import org.jbb.system.api.model.session.UserSession;
import org.jbb.system.api.service.SessionService;
import org.jbb.system.web.session.data.SessionRow;
import org.jbb.system.web.session.form.SessionInActiveTimeForm;
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
    private static final String USER_SESSION_MODEL_ATTRIBUTE = "userSessions";
    private static final String SESSION_FORM_NAME = "sessionSettingForm";

    private static final String MAX_INACTIVE_INTERVAL_SESSION_TIME_MODEL_ATTRIBUTE = "maxinactiveintervalsessiontime";


    public SessionController(SessionService sessionService){
        this.sessionService = sessionService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getSessionView(Model model){

        SessionInActiveTimeForm sessionInActiveTimeForm = new SessionInActiveTimeForm();

        sessionInActiveTimeForm.setMaxInactiveInterval(sessionService.getDefaultInactiveSessionInterval());
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

        model.addAttribute(USER_SESSION_MODEL_ATTRIBUTE,sessionRowList);
        model.addAttribute(SESSION_FORM_NAME,sessionInActiveTimeForm);

        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void saveNewValueOfMaxInActiveIntervalTimeAttribute(SessionInActiveTimeForm sessionInActiveTimeForm){
        Validate.notNull(sessionInActiveTimeForm.getMaxInactiveInterval());

        sessionService.setDefaultInactiveSessionInterval(sessionInActiveTimeForm.getMaxInactiveInterval());

    }
}
