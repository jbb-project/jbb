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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
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
@Import({CommonsConfig.class, MvcConfig.class, EventBusConfig.class, SecurityWebCommonConfig.class})
public class SecurityWebConfig extends GlobalMethodSecurityConfiguration {

    public static final String LOGIN_FAILURE_URL = "/signin?error=true";
    public static final String REMEMBER_ME_KEY = "jbbRememberMe";
    private static final String[] IGNORED_RESOURCES = new String[]{"/fonts/**", "/webjars/**", "/robots.txt"};

    @Lazy
    @Autowired
    private UserDetailsService userDetailsService;

    @Lazy
    @Autowired
    private RefreshableSecurityContextRepository refreshableSecurityContextRepository;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @Lazy
    @Autowired
    private BasicAuthenticationEntryPoint basicAuthenticationEntryPoint;

    @Lazy
    @Autowired
    private LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint;

    @Lazy
    @Autowired
    private RootAuthSuccessHandler rootAuthSuccessHandler;

    @Lazy
    @Autowired
    private RootAuthFailureHandler rootAuthFailureHandler;

    @Lazy
    @Autowired
    private WebMvcMetricsFilter webMvcMetricsFilter;

    @Lazy
    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Lazy
    @Autowired
    private JbbEventBus eventBus;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new OAuth2MethodSecurityExpressionHandler();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        try {
            auth.userDetailsService(userDetailsService);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Bean
    public OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint() {
        return new OAuth2AuthenticationEntryPoint();
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
            String requestUrl = request.getRequestURI();
            return ((auth != null && auth.startsWith("Basic")));
        }
    }

    @Configuration
    public class UiSecurityWebConfig extends WebSecurityConfigurerAdapter {

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() {
            return authenticationManager;
        }

        @Bean
        public RememberMeAuthenticationFilter rememberMeAuthenticationFilter() {
            return new RememberMeAuthenticationFilter(authenticationManager,
                persistentTokenBasedRememberMeServices());
        }

        @Bean
        public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() {
            UsernamePasswordAuthenticationFilter filter =
                new UsernamePasswordAuthenticationFilter();
            filter.setRememberMeServices(persistentTokenBasedRememberMeServices());
            filter.setAuthenticationManager(authenticationManager);
            return filter;
        }

        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers(IGNORED_RESOURCES);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.addFilterBefore(webMvcMetricsFilter, SecurityContextPersistenceFilter.class);
            http.exceptionHandling().authenticationEntryPoint(loginUrlAuthenticationEntryPoint).and()
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

    }

    @Configuration
    @Order(2)
    public class ApiSecurityWebConfig extends WebSecurityConfigurerAdapter {

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() {
            return authenticationManager;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.addFilterBefore(webMvcMetricsFilter, SecurityContextPersistenceFilter.class);
            http
                    .requestMatcher(new BasicRequestMatcher())
                    .httpBasic()
                    .realmName("jBB API")
                    .authenticationEntryPoint(basicAuthenticationEntryPoint)
                    .and()
                    .requestCache().requestCache(new NullRequestCache())
                    .and()
                    .exceptionHandling().authenticationEntryPoint(basicAuthenticationEntryPoint);

            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            http.securityContext().securityContextRepository(refreshableSecurityContextRepository);
            http.csrf().disable();
        }
    }

    @Configuration
    @Order(5)
    public class ApiFallbackSecurityWebConfig extends WebSecurityConfigurerAdapter {

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() {
            return authenticationManager;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.addFilterBefore(webMvcMetricsFilter, SecurityContextPersistenceFilter.class);
            http
                    .antMatcher("/api/**")
                    .httpBasic()
                    .realmName("jBB API")
                    .authenticationEntryPoint(basicAuthenticationEntryPoint)
                    .and()
                    .requestCache().requestCache(new NullRequestCache())
                    .and()
                    .exceptionHandling().authenticationEntryPoint(basicAuthenticationEntryPoint);

            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            http.securityContext().securityContextRepository(refreshableSecurityContextRepository);
            http.csrf().disable();
        }
    }
}
