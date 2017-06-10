/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web;

import org.jbb.lib.mvc.security.RefreshableSecurityContextRepository;
import org.jbb.lib.mvc.security.RootAuthFailureHandler;
import org.jbb.lib.mvc.security.RootAuthSuccessHandler;
import org.jbb.lib.mvc.security.RootLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan("org.jbb.security.web")
public class SecurityWebConfig extends WebSecurityConfigurerAdapter {
    public static final String LOGIN_FAILURE_URL = "/signin?error=true";
    private static final String[] IGNORED_RESOURCES = new String[]{"/fonts/**", "/webjars/**", "/robots.txt"};

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RefreshableSecurityContextRepository refreshableSecurityContextRepository;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private RootAuthSuccessHandler rootAuthSuccessHandler;

    @Autowired
    private RootAuthFailureHandler rootAuthFailureHandler;

    @Autowired
    private RootLogoutSuccessHandler rootLogoutSuccessHandler;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception { //NOSONAR
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/signin");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(IGNORED_RESOURCES);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()).and()
                .formLogin()
                .loginPage("/signin")
                .loginProcessingUrl("/signin/auth")
                .failureUrl(LOGIN_FAILURE_URL)
                .usernameParameter("username")
                .passwordParameter("pswd")
                .and()
                .logout().logoutUrl("/signout")
                .logoutSuccessHandler(rootLogoutSuccessHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/ucp/**").authenticated()
                .antMatchers("/acp/**").hasRole("ADMINISTRATOR")
                .antMatchers("/monitoring/**").hasRole("ADMINISTRATOR");
        http.csrf().csrfTokenRepository(new CookieCsrfTokenRepository());
        http.formLogin().successHandler(rootAuthSuccessHandler);
        http.formLogin().failureHandler(rootAuthFailureHandler);
        http.securityContext().securityContextRepository(refreshableSecurityContextRepository);

        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter, CsrfFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
}
