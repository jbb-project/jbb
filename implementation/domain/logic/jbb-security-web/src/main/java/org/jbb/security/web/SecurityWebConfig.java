/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web;

import org.jbb.lib.mvc.security.AuthenticationFailureHandlerComposite;
import org.jbb.lib.mvc.security.AuthenticationSuccessHandlerComposite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan("org.jbb.web.security")
public class SecurityWebConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private AuthenticationSuccessHandlerComposite authenticationSuccessHandlerComposite;

    @Autowired
    private AuthenticationFailureHandlerComposite authenticationFailureHandlerComposite;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception { //NOSONAR
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/signin");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()).and()
                .formLogin()
                .loginPage("/signin")
                .loginProcessingUrl("/login")
                .usernameParameter("login")
                .passwordParameter("pswd")
                .and()
                .logout().logoutUrl("/logout");
        http.csrf().disable();
        http.formLogin().successHandler(authenticationSuccessHandlerComposite);
        http.formLogin().failureHandler(authenticationFailureHandlerComposite);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
}
