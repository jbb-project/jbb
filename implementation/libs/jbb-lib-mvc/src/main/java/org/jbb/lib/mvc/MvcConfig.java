/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc;

import com.google.common.collect.Sets;

import org.jbb.lib.mvc.properties.MvcProperties;
import org.jbb.lib.properties.ModulePropertiesFactory;
import org.reflections.Reflections;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@Configuration
@ComponentScan("org.jbb.lib.mvc")
public class MvcConfig extends WebMvcConfigurationSupport {
    private static final String ROOT_JBB_PACKAGE = "org.jbb";

    @Bean
    public MvcProperties mvcProperties(ModulePropertiesFactory propertiesFactory) {
        return propertiesFactory.create(MvcProperties.class);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        interceptorRegistryUpdater().fill(registry);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        formatterRegistryUpdater().fill(registry);
    }

    @Bean
    public InterceptorRegistryUpdater interceptorRegistryUpdater() {
        return new InterceptorRegistryUpdater();
    }

    @Bean
    public FormatterRegistryUpdater formatterRegistryUpdater() {
        return new FormatterRegistryUpdater(reflections());
    }

    @Bean
    public ServletContextTemplateResolver servletContextTemplateResolver() {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(getServletContext());
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(servletContextTemplateResolver());
        templateEngine.setAdditionalDialects(Sets.newHashSet(springSecurityDialect()));
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setOrder(1);
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    public Reflections reflections() {
        return new Reflections(ROOT_JBB_PACKAGE);
    }

    @Bean
    public MessageSource messageSource() {
        WildcardReloadableResourceBundleMessageSource messageSource =
                new WildcardReloadableResourceBundleMessageSource();
        String[] baseNames = StringUtils.commaDelimitedListToStringArray("classpath*:**/messages-*");
        messageSource.setBasenames(baseNames);
        return messageSource;
    }
}
