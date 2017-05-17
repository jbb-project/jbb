package org.jbb.system.web.session;

import org.jbb.system.api.model.session.UserSession;
import org.jbb.system.api.service.SessionService;
import org.jbb.system.web.session.data.SessionUITableRow;
import org.jbb.system.web.session.form.InactiveIntervalTimeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
@RequestMapping("/acp/system/sessions")
public class SessionController {

    private SessionService sessionService;

    private static final String VIEW_NAME = "acp/system/sessions";
    private static final String USER_SESSION_MODEL_ATTRIBUTE = "userSessions";
    private static final String SESSION_FORM_NAME = "sessionSettingForm";
    private static final String SESSION_REMOVE_FORM_NAME = "sessionRemoveForm";

    private static final String FORM_SAVED_CORRECT_STATUS_FLAG = "sessionFormSaved";

    @Autowired
    public SessionController(SessionService sessionService){
        this.sessionService = sessionService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getSessionView(Model model){

        InactiveIntervalTimeForm inactiveIntervalTimeForm = new InactiveIntervalTimeForm();

        inactiveIntervalTimeForm.setMaxInactiveIntervalTime(String.valueOf(sessionService.getDefaultInactiveSessionInterval().getSeconds()));
        List<UserSession> userSessionList = sessionService.getAllUserSessions();

        List<SessionUITableRow> sessionUITableRowList = userSessionList.stream()
                .map(userSession ->
                        new SessionUITableRow(userSession.sessionId(),
                                        userSession.creationTime(),
                                        userSession.lastAccessedTime(),
                                        userSession.usedTime(),
                                        userSession.inactiveTime(),
                                        userSession.userName(),
                                        userSession.displayUserName(),
                                        userSession.timeToLive()))
                .collect(Collectors.toList());

        model.addAttribute(USER_SESSION_MODEL_ATTRIBUTE, sessionUITableRowList);
        model.addAttribute(SESSION_FORM_NAME, inactiveIntervalTimeForm);

        return VIEW_NAME;
    }

    @RequestMapping(value = "/setnewvalueofproperties",method = RequestMethod.POST)
    public String saveNewValueOfMaxInActiveIntervalTimeAttribute(@ModelAttribute(SESSION_FORM_NAME)InactiveIntervalTimeForm inactiveIntervalTimeForm,
                                                                 RedirectAttributes redirectAttributes){

        try {
            Long newParameterValue = Long.parseLong(inactiveIntervalTimeForm.getMaxInactiveIntervalTime());
            sessionService.setDefaultInactiveSessionInterval(Duration.ofSeconds(newParameterValue) );
            redirectAttributes.addFlashAttribute(FORM_SAVED_CORRECT_STATUS_FLAG,true);

            return "redirect:/"+VIEW_NAME;
        }
        catch(NumberFormatException ex) {
            log.debug("Wrong value in input field. Should be long!");
            redirectAttributes.addFlashAttribute(FORM_SAVED_CORRECT_STATUS_FLAG,false);
            return VIEW_NAME;
        }
    }

    @RequestMapping(value = "/removesession",method = RequestMethod.POST)
    public String removeSession(@RequestParam("sessionID") String sessionID){

        System.out.println("Abc");
        sessionService.terminateSession(sessionID);
        return "redirect:/"+VIEW_NAME;
    }
}
