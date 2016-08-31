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

import org.jbb.frontend.FrontendConfig;
import org.jbb.frontend.web.FrontendWebConfig;
import org.jbb.lib.core.CoreConfig;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.members.MembersConfig;
import org.jbb.members.web.MembersWebConfig;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

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

        DispatcherServlet dispatcherServlet = new DispatcherServlet(mvcContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);

        ServletRegistration.Dynamic appServlet = servletContext.addServlet(SERVLET_NAME, dispatcherServlet);
        appServlet.setLoadOnStartup(1);
        appServlet.addMapping("/");

        // Spring Security filter chain configuration
        FilterRegistration.Dynamic springSecurityFilterChain = servletContext.addFilter(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME, DelegatingFilterProxy.class);
        springSecurityFilterChain.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
    }
}
