package org.jbb.frontend.impl.logic.stactrace.strategy;

import com.google.common.collect.Lists;
import org.jbb.frontend.api.service.stacktrace.StackTraceVisibilityUsersValues;
import org.jbb.frontend.impl.logic.stacktrace.strategy.AdminStackTraceVisibilityStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminStackTraceVisibilityStrategyTest {

    @Mock
    private UserDetails principal;

    @InjectMocks
    private AdminStackTraceVisibilityStrategy adminStackTraceVisibilityStrategy;

    private final static String ADMINISTRATOR_ROLE = "ADMINISTRATOR";

    @Before
    public void init(){
        principal = Mockito.mock(UserDetails.class);
        defineMockBehaviour(principal);
    }

    @Test
    public void whenUserIsAdministratorAndFilePropertiesSetToAdministratorThenMethodShouldReturnTrue(){
//        //when
//
//        boolean canHandle = adminStackTraceVisibilityStrategy.canHandle(StackTraceVisibilityUsersValues.ADMINISTRATORS, principal);
//
//        //then
//        assertTrue(canHandle);
    }


    private void defineMockBehaviour(UserDetails principal) {
        List<GrantedAuthority> simpleGrantedAuthorities = Lists.newArrayList(new SimpleGrantedAuthority(ADMINISTRATOR_ROLE));

        doReturn(simpleGrantedAuthorities).when(principal.getAuthorities());
    }
}
