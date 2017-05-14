package org.jbb.system.web.session;

import org.jbb.system.api.model.session.UserSession;
import org.jbb.system.api.service.SessionService;
import org.jbb.system.web.session.data.SessionUITableRow;
import org.jbb.system.web.session.form.InactiveIntervalTimeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/acp/system/sessions")
public class SessionController {

    private final Validator validator;
    private SessionService sessionService;

    private static final String VIEW_NAME = "acp/system/sessions";
    private static final String USER_SESSION_MODEL_ATTRIBUTE = "userSessions";
    private static final String SESSION_FORM_NAME = "sessionSettingForm";

    private static final String FORM_SAVED_CORRECT_STATUS_FLAG = "sessionFormSaved";

    @Autowired
    public SessionController(SessionService sessionService, @Qualifier("maxinactiveintervaltimevalidator") Validator validator){
        this.validator=validator;
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

    @RequestMapping(method = RequestMethod.POST)
    public String saveNewValueOfMaxInActiveIntervalTimeAttribute(@ModelAttribute(SESSION_FORM_NAME)InactiveIntervalTimeForm inactiveIntervalTimeForm,
                                                                 BindingResult bindingResult,
                                                                 RedirectAttributes redirectAttributes){

        validator.validate(inactiveIntervalTimeForm,bindingResult);

        if( bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute(FORM_SAVED_CORRECT_STATUS_FLAG,false);
            return VIEW_NAME;
        }

        sessionService.setDefaultInactiveSessionInterval(Duration.ofSeconds( Long.valueOf(inactiveIntervalTimeForm.getMaxInactiveIntervalTime())) );
        redirectAttributes.addFlashAttribute(FORM_SAVED_CORRECT_STATUS_FLAG,true);

        return "redirect:/"+VIEW_NAME;
    }
}
