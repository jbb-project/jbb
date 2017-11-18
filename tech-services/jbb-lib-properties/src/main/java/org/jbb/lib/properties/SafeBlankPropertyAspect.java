/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.Config;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class SafeBlankPropertyAspect {

    @Around("execution(* org.jbb.lib.properties.ModuleProperties.getProperty(..)) && args(key,..)")
    public Object makeSafeBlankProperty(ProceedingJoinPoint joinPoint, String key) {
        log.trace(
            "[PROP-SAFEBLANK-ASPECT ENTERED] Set property '{}' with value '{}'. Join point: {}",
            key, joinPoint.getSignature().toLongString());
        ModuleProperties properties = (ModuleProperties) joinPoint.getTarget();
        String currentProperty = properties.getProperty(key);
        return StringUtils.defaultIfBlank(currentProperty, null);
    }

    @Around("this(org.jbb.lib.properties.ModuleProperties)")
    public Object makeSafeBlankProperty(ProceedingJoinPoint joinPoint) throws Throwable {
        log.trace("[PROP-SAFEBLANK-ASPECT ENTERED] Join point: {}",
            joinPoint.getSignature().toLongString());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Config.Key key = method.getAnnotation(Config.Key.class);
        if (key != null) {
            log.trace("[PROP-SAFEBLANK-ASPECT] Get property '{}'. Join point: {}", key.value(),
                joinPoint.getSignature().toLongString());
            ModuleProperties properties = (ModuleProperties) joinPoint.getThis();
            String value = properties.getProperty(key.value());
            if (StringUtils.isBlank(value)) {
                return null;
            }
        }

        Object object = joinPoint.proceed();
        log.trace("[PROP-SAFEBLANK-ASPECT EXITED] Join point: {}",
            joinPoint.getSignature().toLongString());
        return object;
    }
}
