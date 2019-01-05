/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp;

import org.jbb.install.InstallationAssetsConfig;
import org.jbb.lib.mvc.RequestIdListener;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuration the ServletContext programmatically -- as opposed to (or possibly in conjunction
 * with) the traditional web.xml-based approach
 */
@Slf4j
public class WebAppInitializer extends AbstractHttpSessionApplicationInitializer {

    public static final String SERVLET_NAME = "jbbWebAppServlet";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        log.info("************ Starting jBB Application ************");
        AnnotationConfigWebApplicationContext mvcContext = new AnnotationConfigWebApplicationContext();

        mvcContext.register(InstallationAssetsConfig.class);
        mvcContext.register(LibsCompositeConfig.class);
        mvcContext.register(DomainCompositeConfig.class);
        mvcContext.register(WebCompositeConfig.class);
        mvcContext.register(RestCompositeConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(mvcContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);

        ServletRegistration.Dynamic appServlet = servletContext.addServlet(SERVLET_NAME, dispatcherServlet);
        appServlet.setLoadOnStartup(1);
        appServlet.addMapping("/");
        // setting true for getting SSE streams works
        appServlet.setAsyncSupported(true);

        servletContext.addListener(new RequestIdListener());
        servletContext.addListener(new ContextLoaderListener(mvcContext));
        servletContext.addListener(new RequestContextListener());
        servletContext.addListener(new HttpSessionEventPublisher());

        // it MUST be invoked before spring security filter chain config!
        // AbstractHttpSessionApplicationInitializer registers SessionRepositoryFilter which must be present before spring security filters
        super.onStartup(servletContext);

        // Spring Security filter chain configuration
        FilterRegistration.Dynamic springSecurityFilterChain = servletContext
                .addFilter(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME, DelegatingFilterProxy.class);
        springSecurityFilterChain.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
    }
}
