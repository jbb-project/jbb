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
import org.jbb.lib.core.JbbHomePath;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.members.MembersConfig;
import org.jbb.members.web.MembersWebConfig;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class WebAppInitializer implements WebApplicationInitializer {

    public static final String SERVLET_NAME = "jbbWebAppServlet";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        JbbHomePath.resolveEffectiveAndStoreToSystemProperty();
        JbbHomePath.createIfNotExists();
        AnnotationConfigWebApplicationContext mvcContext = new AnnotationConfigWebApplicationContext();
        mvcContext.register(
                MvcConfig.class, EventBusConfig.class, DbConfig.class,
                FrontendConfig.class, FrontendWebConfig.class,
                MembersConfig.class, MembersWebConfig.class
        );
        ServletRegistration.Dynamic appServlet = servletContext.addServlet(SERVLET_NAME, new DispatcherServlet(mvcContext));
        appServlet.setLoadOnStartup(1);
        appServlet.addMapping("/");
    }
}
