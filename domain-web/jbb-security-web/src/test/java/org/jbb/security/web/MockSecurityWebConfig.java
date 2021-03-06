/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web;

import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.lockout.LockoutSettingsService;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.jbb.security.api.oauth.OAuthClientsService;
import org.jbb.security.api.signin.SignInSettingsService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
public class MockSecurityWebConfig {
    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return Mockito.mock(UserDetailsService.class);
    }

    @Bean
    @Primary
    public ClientDetailsService defaultClientDetailsService() {
        return Mockito.mock(ClientDetailsService.class);
    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    @Primary
    public JbbEventBus jbbEventBus() {
        return Mockito.mock(JbbEventBus.class);
    }

    @Bean
    @Primary
    public MemberService memberService() {
        return Mockito.mock(MemberService.class);
    }

    @Bean
    @Primary
    public MemberLockoutService memberLockoutService() {
        return Mockito.mock(MemberLockoutService.class);
    }

    @Bean
    @Primary
    public LockoutSettingsService lockoutSettingsService() {
        return Mockito.mock(LockoutSettingsService.class);
    }

    @Bean
    @Primary
    public OAuthClientsService oAuthClientsService() {
        return Mockito.mock(OAuthClientsService.class);
    }

    @Bean
    @Primary
    public SignInSettingsService signInSettingsService() {
        return Mockito.mock(SignInSettingsService.class);
    }

    @Bean
    @Primary
    public PersistentTokenRepository persistentTokenRepository() {
        return new InMemoryTokenRepositoryImpl();
    }
}
