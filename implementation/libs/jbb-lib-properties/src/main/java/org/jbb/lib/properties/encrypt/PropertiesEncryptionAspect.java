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
import org.jbb.lib.properties.ModuleProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

import static org.jbb.lib.properties.encrypt.EncryptionPlaceholderUtils.isInDecPlaceholder;
import static org.jbb.lib.properties.encrypt.EncryptionPlaceholderUtils.surroundWithEncPlaceholder;

@Aspect
@Slf4j
@Component
public class PropertiesEncryptionAspect {
    private final PropertiesEncryption propertiesEncryption;
    private final PropertyTypeCasting typeCasting;

    @Autowired
    public PropertiesEncryptionAspect(PropertiesEncryption propertiesEncryption,
                                      PropertyTypeCasting typeCasting) {
        this.propertiesEncryption = propertiesEncryption;
        this.typeCasting = typeCasting;
    }

    @Around("execution(* org.jbb.lib.properties.ModuleProperties.setProperty(..)) && args(key, value,..)")
    public void logAroundAllMethods(ProceedingJoinPoint joinPoint, String key, String value) throws Throwable {
        log.debug("[PROP-ENC-ASPECT ENTERED] Set property '{}' with value '{}'. Join point: {}", key, value, joinPoint.getSignature().toLongString());
        ModuleProperties properties = (ModuleProperties) joinPoint.getTarget();
        String currentProperty = properties.getProperty(key);
        if (isInDecPlaceholder(currentProperty)) {
            joinPoint.proceed(new Object[]{key, propertiesEncryption.encryptIfNeeded(surroundWithEncPlaceholder(value))});
        } else {
            joinPoint.proceed();
        }
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
            ModuleProperties properties = (ModuleProperties) joinPoint.getThis();
            String value = properties.getProperty(key.value());
            if (isInDecPlaceholder(value)) {
                String decryptedString = propertiesEncryption.decryptIfNeeded(value);
                Class returnType = ((MethodSignature) joinPoint.getSignature()).getReturnType();
                return typeCasting.resolve(decryptedString, returnType);
            }
        }

        Object object = joinPoint.proceed();
        log.debug("[PROP-ENC-ASPECT EXITED] Join point: {}", joinPoint.getSignature().toLongString());
        return object;
    }
}
