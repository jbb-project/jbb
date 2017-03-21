/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
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
public class WebAppInitializer implements WebApplicationInitializer {

    public static final String SERVLET_NAME = "jbbWebAppServlet";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        log.info("************ Starting jBB Application ************");
        AnnotationConfigWebApplicationContext mvcContext = new AnnotationConfigWebApplicationContext();

        mvcContext.register(LibsCompositeConfig.class);
        mvcContext.register(DomainCompositeConfig.class);
        mvcContext.register(WebCompositeConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(mvcContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);

        ServletRegistration.Dynamic appServlet = servletContext.addServlet(SERVLET_NAME, dispatcherServlet);
        appServlet.setLoadOnStartup(1);
        appServlet.addMapping("/");

        servletContext.addListener(new ContextLoaderListener(mvcContext));

        // Spring Security filter chain configuration
        FilterRegistration.Dynamic springSecurityFilterChain = servletContext
                .addFilter(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME, DelegatingFilterProxy.class);
        springSecurityFilterChain.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
    }
}
