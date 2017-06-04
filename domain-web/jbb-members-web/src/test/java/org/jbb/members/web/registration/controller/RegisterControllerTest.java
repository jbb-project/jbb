/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.registration.controller;

import com.google.common.collect.Maps;

import org.jbb.members.api.data.RegistrationRequest;
import org.jbb.members.api.exception.RegistrationException;
import org.jbb.members.api.service.RegistrationService;
import org.jbb.members.web.registration.form.RegisterForm;
import org.jbb.members.web.registration.logic.RegistrationErrorsBindingMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RegisterControllerTest {
    @Mock
    private RegistrationService registrationServiceMock;

    @Mock
    private RegistrationErrorsBindingMapper errorsBindingMapperMock;

    @InjectMocks
    private RegisterController registerController;

    @Test
    public void shouldGoToMainPage_whenSignInUserOpenedRegistrationPage() throws Exception {
        // given
        Model modelMock = mock(Model.class);
        Authentication authenticationMock = mock(Authentication.class); // user authenticated
        given(authenticationMock.isAuthenticated()).willReturn(true);

        // when
        String viewName = registerController.signUp(modelMock, authenticationMock);

        // then
        assertThat(viewName).isEqualTo("redirect:/");
    }

    @Test
    public void shouldPutToModelEmptyForm_whenNotSignInUserOpenedRegistrationPage() throws Exception {
        // given
        Model modelMock = mock(Model.class);
        Authentication authenticationMock = null; // anonim user

        // when
        registerController.signUp(modelMock, authenticationMock);

        // then
        verify(modelMock, times(1)).addAttribute(eq("registerForm"), any(RegisterForm.class));
    }

    @Test
    public void shouldSetRegisterCompleteStatusToFalse_whenNotSignInUserOpenedRegistrationPage() throws Exception {
        // given
        Model modelMock = mock(Model.class);
        Authentication authenticationMock = null; // anonim user

        // when
        registerController.signUp(modelMock, authenticationMock);

        // then
        verify(modelMock, times(1)).addAttribute(eq("registrationCompleted"), eq(false));
    }

    @Test
    public void shouldReturnRegistrationViewName_whenNotSignInUserOpenedRegistrationPage() throws Exception {
        // given
        Model modelMock = mock(Model.class);
        Authentication authenticationMock = null; // anonim user

        // when
        String viewName = registerController.signUp(modelMock, authenticationMock);

        // then
        assertThat(viewName).isEqualTo("register");
    }

    @Test
    public void shouldAddFlashAttributeWithMemberUsername_whenRegistrationCompleted() throws Exception {
        // given
        RedirectAttributes redirectAttributesMock = mock(RedirectAttributes.class);

        RegisterForm registerFormMock = mock(RegisterForm.class);
        given(registerFormMock.getUsername()).willReturn("john");

        // when
        registerController.processRegisterForm(mock(Model.class), registerFormMock, mock(BindingResult.class),
                mock(HttpServletRequest.class), redirectAttributesMock);

        // then
        verify(redirectAttributesMock, times(1)).addFlashAttribute(eq("newMemberUsername"), eq("john"));
    }

    @Test
    public void shouldRedirectToSuccessPage_whenRegistrationCompleted() throws Exception {
        // given
        RedirectAttributes redirectAttributesMock = mock(RedirectAttributes.class);

        RegisterForm registerFormMock = mock(RegisterForm.class);
        given(registerFormMock.getUsername()).willReturn("john");

        // when
        String viewName = registerController.processRegisterForm(mock(Model.class), registerFormMock, mock(BindingResult.class),
                mock(HttpServletRequest.class), redirectAttributesMock);

        // then
        assertThat(viewName).isEqualTo("redirect:/register/success");
    }

    @Test
    public void shouldReturnToRegistrationPage_whenRegistrationFailed() throws Exception {
        // given
        RegistrationException registrationExceptionMock = mock(RegistrationException.class);
        doThrow(registrationExceptionMock).when(registrationServiceMock).register(any(RegistrationRequest.class));

        // when
        String viewName = registerController.processRegisterForm(mock(Model.class), mock(RegisterForm.class), mock(BindingResult.class),
                mock(HttpServletRequest.class), mock(RedirectAttributes.class));

        // then
        assertThat(viewName).isEqualTo("register");
    }

    @Test
    public void shouldRedirectToRegistrationPage_whenSuccessPageInvokedManually() throws Exception {
        // given
        Model modelMock = mock(Model.class);
        given(modelMock.asMap()).willReturn(Maps.newHashMap()); // no new member name key

        // when
        String viewName = registerController.signUpSuccess(modelMock);

        // then
        assertThat(viewName).isEqualTo("redirect:/register");
    }

    @Test
    public void shouldSetRegisterCompleteStatusToTrue_whenMemberUsernameExists() throws Exception {
        // given
        Model modelMock = mock(Model.class);

        given(modelMock.asMap()).willAnswer(invocationOnMock -> {
            HashMap<Object, Object> map1 = Maps.newHashMap();
            map1.put("newMemberUsername", "john");
            return map1;
        });

        // when
        registerController.signUpSuccess(modelMock);

        // then
        verify(modelMock, times(1)).addAttribute(eq("registrationCompleted"), eq(true));
    }
}