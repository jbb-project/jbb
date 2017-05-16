package org.jbb.system.web.session;

import com.google.common.collect.Lists;

import org.jbb.lib.core.CoreConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.jbb.lib.test.SpringSecurityConfigMocks;
import org.jbb.system.api.model.session.UserSession;
import org.jbb.system.api.service.SessionService;
import org.jbb.system.web.SystemConfigMock;
import org.jbb.system.web.SystemWebConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CoreConfig.class, MvcConfig.class, SystemWebConfig.class, PropertiesConfig.class,
        SystemConfigMock.class, CoreConfigMocks.class, SpringSecurityConfigMocks.class})
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class SessionControllerIT {

    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private SessionService sessionService;

    @Before
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void whenGetAllSessionMethodIsInvokedThenOkStatusShouldBeReturned() throws Exception {

        //given
        Duration oneHourDuration = Duration.ofHours(1);
        List<UserSession> userSessionList = createUserSessionList(2);
        when(sessionService.getDefaultInactiveSessionInterval()).thenReturn(oneHourDuration);
        when(sessionService.getAllUserSessions()).thenReturn(userSessionList);

        //when
        ResultActions resultActions = mockMvc.perform(get("/acp/system/sessions"));

        //then
        resultActions.andExpect(status().isOk())
                     .andExpect(view().name("acp/system/sessions"));
    }

    @Test
    public void whenSaveNewValueOfMaxInActiveIntervalTimeAttributeMethodIsInvokedWithCorrectFormatOfInputValueThenOkStatusShouldBeReturned(){


        //given

        //when

        //then
    }

    @Test
    public void whenSaveNewValueOfMaxInActiveIntervalTimeAttributeMethodIsInvokedWithThenFlashAttributeShouldBeSet(){

        //given

        //when

        //then
    }

    private List<UserSession> createUserSessionList(int numberOfSessionToCreate) {
        ArrayList<UserSession> arrayList = Lists.newArrayList();

        for(int i =0;i<numberOfSessionToCreate;i++){
            final int temp = i;
            arrayList.add(new UserSession() {
                @Override
                public String sessionId() {
                    return "sessionId"+temp;
                }

                @Override
                public LocalDateTime creationTime() {
                    return LocalDateTime.of(2017,05,16,temp,temp);
                }

                @Override
                public LocalDateTime lastAccessedTime() {
                    return LocalDateTime.of(2017,05,16,temp,temp);
                }

                @Override
                public Duration usedTime() {
                    return Duration.ofHours(temp);
                }

                @Override
                public Duration inactiveTime() {
                    return Duration.ofHours(temp);
                }

                @Override
                public Duration timeToLive() {
                    return Duration.ofHours(temp);
                }

                @Override
                public String userName() {
                    return "username"+temp;
                }

                @Override
                public String displayUserName() {
                    return "displayName"+temp;
                }
            });
        }
        return arrayList;
    }


}