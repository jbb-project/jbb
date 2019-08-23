/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc;

import com.google.common.collect.Sets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.metrics.MetricsConfig;
import org.jbb.lib.mvc.session.JbbSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.web.util.UrlPathHelper;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.util.List;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.spring.web.servlet.DefaultWebMvcTagsProvider;
import io.micrometer.spring.web.servlet.WebMvcMetricsFilter;
import io.micrometer.spring.web.servlet.WebMvcTagsProvider;

@Configuration
@EnableSpringHttpSession
@EnableSpringDataWebSupport
@ComponentScan
@Import({CommonsConfig.class, MetricsConfig.class})
public class MvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    private InterceptorRegistryUpdater interceptorRegistryUpdater;

    @Autowired
    private FormatterRegistryUpdater formatterRegistryUpdater;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        interceptorRegistryUpdater.fill(registry);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        formatterRegistryUpdater.fill(registry);
    }

    @Bean
    public StringHttpMessageConverter stringMessageConverter() {
        return new StringHttpMessageConverter();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        converters.add(converter);
        converters.add(stringMessageConverter());
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
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
    public MessageSource messageSource() {
        WildcardReloadableResourceBundleMessageSource messageSource =
                new WildcardReloadableResourceBundleMessageSource();
        String[] baseNames = StringUtils.commaDelimitedListToStringArray("classpath*:**/messages-*");
        messageSource.setBasenames(baseNames);
        return messageSource;
    }

    @Bean
    public SessionRepository sessionRepository(ApplicationEventPublisher eventPublisher) {
        return new JbbSessionRepository(eventPublisher);
    }

    @Bean
    public UrlPathHelper urlPathHelper() {
        return new UrlPathHelper();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 20));
        argumentResolvers.add(resolver);
    }

    @Bean
    public DefaultWebMvcTagsProvider servletTagsProvider() {
        return new DefaultWebMvcTagsProvider();
    }

    @Bean
    public HandlerMappingIntrospector handlerMappingIntrospector(WebApplicationContext ctx) {
        HandlerMappingIntrospector hmi = new HandlerMappingIntrospector();
        hmi.setApplicationContext(ctx);
        return hmi;
    }

    @Bean
    public WebMvcMetricsFilter webMetricsFilter(MeterRegistry registry,
                                                WebMvcTagsProvider tagsProvider,
                                                HandlerMappingIntrospector handlerMappingIntrospector) {

        return new WebMvcMetricsFilter(registry, tagsProvider,
                "request",
                true,
                handlerMappingIntrospector);
    }

}
