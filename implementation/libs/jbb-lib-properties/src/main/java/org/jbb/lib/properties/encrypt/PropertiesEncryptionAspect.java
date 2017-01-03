/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties.encrypt;

import org.aeonbits.owner.Config;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class PropertiesEncryptionAspect {//TODO

    @Around("execution(* org.jbb.lib.properties.ModuleProperties.setProperty(..)) && args(key, value,..)")
    public void logAroundAllMethods(ProceedingJoinPoint joinPoint, String key, String value) throws Throwable {
        log.debug("[PROP-ENC-ASPECT ENTERED] Set property '{}' with value '{}'. Join point: {}", key, value, joinPoint.getSignature().toLongString());
        joinPoint.proceed();
        log.debug("[PROP-ENC-ASPECT EXITED] Set property '{}' with value '{}'. Join point: {}", key, value, joinPoint.getSignature().toLongString());
    }

    @Around("this(org.jbb.lib.properties.ModuleProperties)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("[PROP-ENC-ASPECT ENTERED] Join point: {}", joinPoint.getSignature().toLongString());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Config.Key key = method.getAnnotation(Config.Key.class);
        if (key != null) {
            log.debug("[PROP-ENC-ASPECT] Get property '{}'. Join point: {}", key.value(), joinPoint.getSignature().toLongString());
        }

        Object object = joinPoint.proceed();
        log.debug("[PROP-ENC-ASPECT EXITED] Join point: {}", joinPoint.getSignature().toLongString());
        return object;
    }
}
