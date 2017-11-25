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

import static org.jbb.lib.properties.encrypt.EncryptionPlaceholderUtils.isInDecPlaceholder;
import static org.jbb.lib.properties.encrypt.EncryptionPlaceholderUtils.surroundWithEncPlaceholder;

import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.Config;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.jbb.lib.properties.ModuleProperties;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class PropertiesEncryptionAspect {
    private final PropertiesEncryption propertiesEncryption;
    private final PropertyTypeCasting typeCasting;

    @Around("execution(* org.jbb.lib.properties.ModuleProperties.setProperty(..)) && args(key, value,..)")
    public void logAroundAllMethods(ProceedingJoinPoint joinPoint, String key, String value) throws Throwable {
        log.trace("[PROP-ENC-ASPECT ENTERED] Set property '{}' with value '{}'. Join point: {}",
            key, value, joinPoint.getSignature().toLongString());
        ModuleProperties properties = (ModuleProperties) joinPoint.getTarget();
        String currentProperty = properties.getProperty(key);
        if (isInDecPlaceholder(currentProperty)) {
            String newEncryptedValue = propertiesEncryption.encryptIfNeeded(surroundWithEncPlaceholder(value));
            joinPoint.proceed(new Object[]{key, newEncryptedValue});
            log.trace(
                "[PROP-ENC-ASPECT EXITED] Set property '{}' with encrypted value '{}'. Join point: {}",
                key, newEncryptedValue, joinPoint.getSignature().toLongString());
        } else {
            joinPoint.proceed();
            log.trace("[PROP-ENC-ASPECT EXITED] Set property '{}' with value '{}'. Join point: {}",
                key, value, joinPoint.getSignature().toLongString());
        }
    }

    @Around("this(org.jbb.lib.properties.ModuleProperties)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.trace("[PROP-ENC-ASPECT ENTERED] Join point: {}",
            joinPoint.getSignature().toLongString());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Config.Key key = method.getAnnotation(Config.Key.class);
        if (key != null) {
            log.trace("[PROP-ENC-ASPECT] Get property '{}'. Join point: {}", key.value(),
                joinPoint.getSignature().toLongString());
            ModuleProperties properties = (ModuleProperties) joinPoint.getThis();
            String value = properties.getProperty(key.value());
            if (isInDecPlaceholder(value)) {
                String decryptedString = propertiesEncryption.decryptIfNeeded(value);
                Class returnType = ((MethodSignature) joinPoint.getSignature()).getReturnType();
                return typeCasting.resolve(decryptedString, returnType);
            }
        }

        Object object = joinPoint.proceed();
        log.trace("[PROP-ENC-ASPECT EXITED] Join point: {}",
            joinPoint.getSignature().toLongString());
        return object;
    }
}
