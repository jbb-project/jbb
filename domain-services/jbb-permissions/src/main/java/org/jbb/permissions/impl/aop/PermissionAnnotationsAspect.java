/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jbb.permissions.api.PermissionService;
import org.jbb.permissions.api.annotation.AdministratorPermissionRequired;
import org.jbb.permissions.api.annotation.MemberPermissionRequired;
import org.jbb.permissions.api.permission.PermissionDefinition;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class PermissionAnnotationsAspect {

    private final PermissionService permissionService;

    @Pointcut(value = "execution(* *(..))")
    private void anyMethod() { //NOSONAR
        //empty method for aspectj purposes
    }

    @Before("anyMethod() && @annotation(administratorPermissionRequired)")
    public void checkAdministratorPermissionForClassAnnotated(
            AdministratorPermissionRequired administratorPermissionRequired) {
        check(administratorPermissionRequired.value());
    }

    @Before("anyMethod() && @within(administratorPermissionRequired)")
    public void checkAdministratorPermissionForMethodAnnotated(
            AdministratorPermissionRequired administratorPermissionRequired) {
        check(administratorPermissionRequired.value());
    }

    @Before("anyMethod() && @annotation(memberPermissionRequired)")
    public void checkMemberPermissionForClassAnnotated(
            MemberPermissionRequired memberPermissionRequired) {
        check(memberPermissionRequired.value());
    }

    @Before("anyMethod() && @within(memberPermissionRequired)")
    public void checkMemberPermissionForMethodAnnotated(
            MemberPermissionRequired memberPermissionRequired) {
        check(memberPermissionRequired.value());
    }

    private void check(PermissionDefinition permission) {
        permissionService.assertPermission(permission);
    }


}
