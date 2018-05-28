/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web;

import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.mvc.security.RefreshableSecurityContextRepository;
import org.jbb.lib.mvc.security.RootAuthFailureHandler;
import org.jbb.lib.mvc.security.RootAuthSuccessHandler;
import org.jbb.security.web.rememberme.EventAwareTokenBasedRememberMeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;

import io.micrometer.spring.web.servlet.WebMvcMetricsFilter;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan
@Import({CommonsConfig.class, MvcConfig.class, EventBusConfig.class})
public class SecurityWebConfig {
    public static final String LOGIN_FAILURE_URL = "/signin?error=true";
    public static final String REMEMBER_ME_KEY = "jbbRememberMe";
    private static final String[] IGNORED_RESOURCES = new String[]{"/fonts/**", "/webjars/**", "/robots.txt"};

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RefreshableSecurityContextRepository refreshableSecurityContextRepository;

    @Autowired
    @Qualifier("authProvider")
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private RootAuthSuccessHandler rootAuthSuccessHandler;

    @Autowired
    private RootAuthFailureHandler rootAuthFailureHandler;

    @Autowired
    private WebMvcMetricsFilter webMvcMetricsFilter;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Autowired
    private JbbEventBus eventBus;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        try {
            auth.userDetailsService(userDetailsService);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/signin");
    }

    @Bean
    public OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint() {
        return new OAuth2AuthenticationEntryPoint();
    }

    @Bean
    public BasicAuthenticationEntryPoint basicAuthenticationEntryPoint() {
        BasicAuthenticationEntryPoint basicAuthenticationEntryPoint = new BasicAuthenticationEntryPoint();
        basicAuthenticationEntryPoint.setRealmName("jBB API Realm");
        return basicAuthenticationEntryPoint;
    }

    @Bean
    public RememberMeServices persistentTokenBasedRememberMeServices() {
        return new EventAwareTokenBasedRememberMeServices(REMEMBER_ME_KEY, userDetailsService,
            persistentTokenRepository, eventBus);
    }

    private static class BasicRequestMatcher implements RequestMatcher {
        @Override
        public boolean matches(HttpServletRequest request) {
            String auth = request.getHeader("Authorization");
            return (auth != null && auth.startsWith("Basic"));
        }
    }

    @Configuration
    public class UiSecurityWebConfig extends WebSecurityConfigurerAdapter {

        @Bean
        public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
            return new RememberMeAuthenticationProvider(REMEMBER_ME_KEY);
        }

        @Bean
        public RememberMeAuthenticationFilter rememberMeAuthenticationFilter() throws Exception {
            return new RememberMeAuthenticationFilter(authenticationManager(),
                persistentTokenBasedRememberMeServices());
        }

        @Bean
        public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter()
            throws Exception {
            UsernamePasswordAuthenticationFilter filter =
                new UsernamePasswordAuthenticationFilter();
            filter.setRememberMeServices(persistentTokenBasedRememberMeServices());
            filter.setAuthenticationManager(authenticationManager());
            return filter;
        }

        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers(IGNORED_RESOURCES);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.addFilterBefore(webMvcMetricsFilter, SecurityContextPersistenceFilter.class);
            http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()).and()
                    .formLogin()
                    .loginPage("/signin")
                    .loginProcessingUrl("/signin/auth")
                    .failureUrl(LOGIN_FAILURE_URL)
                    .usernameParameter("username")
                    .passwordParameter("pswd")
                    .and()
                    .logout().logoutUrl("/signout")
                    .and()
                    .authorizeRequests()
                    .antMatchers("/ucp/**").authenticated()
                    .antMatchers("/acp/**").hasRole("ADMINISTRATOR")
                    .antMatchers("/monitoring/**").hasRole("ADMINISTRATOR");
            http.csrf().csrfTokenRepository(new CookieCsrfTokenRepository());
            http.formLogin().successHandler(rootAuthSuccessHandler);
            http.formLogin().failureHandler(rootAuthFailureHandler);

            http.securityContext().securityContextRepository(refreshableSecurityContextRepository);

            http.rememberMe()
                .rememberMeParameter("remember-me")
                .rememberMeServices(persistentTokenBasedRememberMeServices())
                .tokenRepository(persistentTokenRepository);

            CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
            characterEncodingFilter.setEncoding("UTF-8");
            characterEncodingFilter.setForceEncoding(true);
            http.addFilterBefore(characterEncodingFilter, CsrfFilter.class);

            http.addFilter(usernamePasswordAuthenticationFilter());
            http.addFilter(rememberMeAuthenticationFilter());

        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) {
            auth.authenticationProvider(authenticationProvider);
            auth.authenticationProvider(rememberMeAuthenticationProvider());
        }
    }

    @Configuration
    @Order(1)
    public class ApiSecurityWebConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.addFilterBefore(webMvcMetricsFilter, SecurityContextPersistenceFilter.class);
            http.requestMatcher(new BasicRequestMatcher())
                    .httpBasic()
                    .realmName("jBB API")
                    .authenticationEntryPoint(oAuth2AuthenticationEntryPoint())
                    .and()
                    .requestCache().requestCache(new NullRequestCache())
                    .and()
                    .exceptionHandling().authenticationEntryPoint(oAuth2AuthenticationEntryPoint());
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            http.securityContext().securityContextRepository(refreshableSecurityContextRepository);
            http.csrf().disable();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) {
            auth.authenticationProvider(authenticationProvider);
        }
    }
}
