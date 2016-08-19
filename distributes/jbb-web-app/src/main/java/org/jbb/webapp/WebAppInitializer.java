/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp;

import org.jbb.lib.core.CoreConfig;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Configuration the ServletContext programmatically -- as opposed to (or possibly in conjunction
 * with) the traditional web.xml-based approach
 */
public class WebAppInitializer implements WebApplicationInitializer {

    public static final String SERVLET_NAME = "jbbWebAppServlet";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext mvcContext = new AnnotationConfigWebApplicationContext();
        // CoreConfig must be register as first due to responsibility
        // of creating jBB working directory and putting default configuration
        mvcContext.register(CoreConfig.class);
        mvcContext.register(LibsCompositeConfig.class);
        mvcContext.register(DomainCompositeConfig.class);
        ServletRegistration.Dynamic appServlet = servletContext.addServlet(SERVLET_NAME, new DispatcherServlet(mvcContext));
        appServlet.setLoadOnStartup(1);
        appServlet.addMapping("/");

        // Spring Security filter chain configuration
        FilterRegistration.Dynamic springSecurityFilterChain = servletContext.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class);
        springSecurityFilterChain.addMappingForUrlPatterns(null, false, "/*");
    }
}
