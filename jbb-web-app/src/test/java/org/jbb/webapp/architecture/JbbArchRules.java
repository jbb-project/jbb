/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.priority;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.base.PackageMatcher;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.Priority;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import io.swagger.annotations.ApiOperation;
import java.lang.annotation.Annotation;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.aeonbits.owner.Config;
import org.hibernate.envers.Audited;
import org.jbb.lib.db.domain.BaseEntity;
import org.jbb.lib.db.revision.RevisionInfo;
import org.jbb.lib.eventbus.JbbEvent;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.permissions.api.annotation.AdministratorPermissionRequired;
import org.jbb.permissions.api.annotation.MemberPermissionRequired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

public class JbbArchRules {

    public static final String TECH_LIBS_LAYER = "Tech libs Layer";
    public static final String API_LAYER = "API Layer";
    public static final String EVENT_API_LAYER = "Event API Layer";
    public static final String SERVICES_LAYER = "Services Layer";
    public static final String WEB_LAYER = "Web Layer";
    public static final String REST_LAYER = "REST Layer";
    public static final String APP_INIT_LAYER = "App Initializer Layer";

    public static final String TECH_LIBS_PACKAGES = "org.jbb.lib..";
    public static final String API_PACKAGES = "org.jbb.(*).api..";
    public static final String EVENT_API_PACKAGES = "org.jbb.(*).event..";
    public static final String SERVICES_PACKAGES = "org.jbb.(*).impl..";
    public static final String WEB_PACKAGES = "org.jbb.(*).web..";
    public static final String REST_PACKAGES = "org.jbb.(*).rest..";
    public static final String APP_INIT_PACKAGES = "org.jbb.webapp..";

    @ArchTest
    public static void testLayeredArchitecture(JavaClasses classes) {
        layeredArchitecture()
                .layer(TECH_LIBS_LAYER).definedBy(TECH_LIBS_PACKAGES)
                .layer(API_LAYER).definedBy(API_PACKAGES)
                .layer(EVENT_API_LAYER).definedBy(EVENT_API_PACKAGES)
                .layer(SERVICES_LAYER).definedBy(SERVICES_PACKAGES)
                .layer(WEB_LAYER).definedBy(WEB_PACKAGES)
                .layer(REST_LAYER).definedBy(REST_PACKAGES)
                .layer(APP_INIT_LAYER).definedBy(APP_INIT_PACKAGES)

                .whereLayer(TECH_LIBS_LAYER)
                .mayOnlyBeAccessedByLayers(
                        SERVICES_LAYER, WEB_LAYER, REST_LAYER, EVENT_API_LAYER, APP_INIT_LAYER)
            .whereLayer(API_LAYER)
            .mayOnlyBeAccessedByLayers(SERVICES_LAYER, WEB_LAYER, REST_LAYER, APP_INIT_LAYER)
                .whereLayer(EVENT_API_LAYER)
                .mayOnlyBeAccessedByLayers(SERVICES_LAYER, WEB_LAYER, REST_LAYER)
                .whereLayer(SERVICES_LAYER).mayNotBeAccessedByAnyLayer()
                .whereLayer(WEB_LAYER).mayNotBeAccessedByAnyLayer()
                .whereLayer(REST_LAYER).mayNotBeAccessedByAnyLayer()
                .whereLayer(APP_INIT_LAYER).mayNotBeAccessedByAnyLayer()

                .check(classes);
    }

    @ArchTest
    public static void serviceLayerShouldNotUseWebLayer(JavaClasses classes) {
        priority(Priority.HIGH).noClasses()
                .that().resideInAPackage(SERVICES_PACKAGES)
                .should().accessClassesThat().resideInAPackage(WEB_PACKAGES)
                .check(classes);
    }

    @ArchTest
    public static void serviceLayerShouldNotUseRestLayer(JavaClasses classes) {
        priority(Priority.HIGH).noClasses()
                .that().resideInAPackage(SERVICES_PACKAGES)
                .should().accessClassesThat().resideInAPackage(REST_PACKAGES)
                .check(classes);
    }

    @ArchTest
    public static void webLayerShouldNotUseServiceLayer(JavaClasses classes) {
        priority(Priority.HIGH).noClasses()
                .that().resideInAPackage(WEB_PACKAGES)
                .should().accessClassesThat().resideInAPackage(SERVICES_PACKAGES)
                .check(classes);
    }

    @ArchTest
    public static void webLayerShouldNotUseRestLayer(JavaClasses classes) {
        priority(Priority.HIGH).noClasses()
                .that().resideInAPackage(WEB_PACKAGES)
                .should().accessClassesThat().resideInAPackage(REST_PACKAGES)
                .check(classes);
    }

    @ArchTest
    public static void restLayerShouldNotUseServiceLayer(JavaClasses classes) {
        priority(Priority.HIGH).noClasses()
                .that().resideInAPackage(REST_PACKAGES)
                .should().accessClassesThat().resideInAPackage(SERVICES_PACKAGES)
                .check(classes);
    }

    @ArchTest
    public static void restLayerShouldNotUseWebLayer(JavaClasses classes) {
        priority(Priority.HIGH).noClasses()
                .that().resideInAPackage(REST_PACKAGES)
                .should().accessClassesThat().resideInAPackage(WEB_PACKAGES)
                .check(classes);
    }

    @ArchTest
    public static void controllersShouldNotUseRepositoriesDirectly(JavaClasses classes) {
        priority(Priority.HIGH).noClasses().that().areAnnotatedWith(Controller.class)
                .should().accessClassesThat().areAnnotatedWith(Repository.class)
                .check(classes);
    }

    @ArchTest
    public static void restControllersShouldNotUseRepositoriesDirectly(JavaClasses classes) {
        priority(Priority.HIGH).noClasses().that().areAnnotatedWith(RestController.class)
                .should().accessClassesThat().areAnnotatedWith(Repository.class)
                .check(classes);
    }

    @ArchTest
    public static void controllerNameShouldEndsWithController(JavaClasses classes) {
        priority(Priority.LOW).classes().that().areAnnotatedWith(Controller.class)
                .should().haveNameMatching(".*Controller")
                .check(classes);
    }

    @ArchTest
    public static void restControllerNameShouldEndsWithResource(JavaClasses classes) {
        priority(Priority.LOW).classes().that().areAnnotatedWith(RestController.class)
                .should().haveNameMatching(".*Resource")
                .check(classes);
    }

    @ArchTest
    public static void entitiesShouldExtendBaseEntity(JavaClasses classes) {
        priority(Priority.MEDIUM).classes().that(areEntity()).and(notBe(RevisionInfo.class))
            .should().beAssignableTo(BaseEntity.class)
                .check(classes);
    }

    @ArchTest
    public static void entitiesShouldBeAudited(JavaClasses classes) {
        priority(Priority.HIGH).classes().that(areEntity()).and(notBe(RevisionInfo.class))
                .should().beAnnotatedWith(Audited.class)
                .check(classes);
    }

    @ArchTest
    public static void entityNameShouldEndsWithEntity(JavaClasses classes) {
        priority(Priority.LOW).classes().that(areEntity()).and(notBe(RevisionInfo.class))
                .should().haveNameMatching(".*Entity")
                .check(classes);
    }

    @ArchTest
    public static void allClassesMustBeInOrgJbbPackage(JavaClasses classes) {
        priority(Priority.MEDIUM).classes()
                .should().resideInAPackage("org.jbb..").check(classes);
    }

    @ArchTest
    public static void springConfigurationClassNameShouldEndsWithConfig(JavaClasses classes) {
        priority(Priority.LOW).classes().that().areAnnotatedWith(Configuration.class)
                .should().haveNameMatching(".*Config")
                .check(classes);
    }

    @ArchTest
    public static void ownerConfigurationClassNameShouldEndsWithProperties(JavaClasses classes) {
        priority(Priority.LOW).classes().that().areAssignableTo(Config.class)
                .should().haveNameMatching(".*Properties")
                .check(classes);
    }

    @ArchTest
    public static void jbbDomainEventClassNameShouldEndsWithEvent(JavaClasses classes) {
        priority(Priority.LOW).classes().that().areAssignableTo(JbbEvent.class)
                .should().haveNameMatching(".*Event")
                .check(classes);
    }

    @ArchTest
    public static void libModulesCannotHaveCycle(JavaClasses classes) {
        slices().matching(TECH_LIBS_PACKAGES).namingSlices("$1 lib")
                .as(TECH_LIBS_LAYER).should().beFreeOfCycles().check(classes);
    }

    @ArchTest
    public static void apiModuleCannotUseAnotherApiModule(JavaClasses classes) {
        slices().matching(API_PACKAGES).namingSlices("$1 api")
                .as(API_LAYER).should().notDependOnEachOther().check(classes);
    }

    @ArchTest
    public static void eventApiModuleCannotUseAnotherEventApiModule(JavaClasses classes) {
        slices().matching(EVENT_API_PACKAGES).namingSlices("$1 event api")
                .as(EVENT_API_LAYER).should().notDependOnEachOther().check(classes);
    }

    @ArchTest
    public static void serviceModuleCannotUseAnotherServiceModule(JavaClasses classes) {
        slices().matching(SERVICES_PACKAGES).namingSlices("$1 service")
                .as(SERVICES_LAYER).should().notDependOnEachOther().check(classes);
    }

    @ArchTest
    public static void webModuleCannotUseAnotherWebModule(JavaClasses classes) {
        slices().matching(WEB_PACKAGES).namingSlices("$1 web")
                .as(WEB_LAYER).should().notDependOnEachOther().check(classes);
    }

    @ArchTest
    public static void restModuleCannotUseAnotherRestModule(JavaClasses classes) {
        slices().matching(REST_PACKAGES).namingSlices("$1 rest")
                .as(REST_LAYER).should().notDependOnEachOther().check(classes);
    }

    @ArchTest
    public static void serviceLayerShouldNotBeSecuredWithAdministratorPermissionRequiredAnnotation(
            JavaClasses classes) {
        priority(Priority.HIGH).classes()
                .that().resideInAPackage(SERVICES_PACKAGES)
                .should().notBeAnnotatedWith(AdministratorPermissionRequired.class)
                .andShould(notHaveMethodAnnotatedWith(AdministratorPermissionRequired.class))
                .check(classes);
    }

    @ArchTest
    public static void serviceLayerShouldNotBeSecuredWithMemberPermissionRequiredAnnotation(
            JavaClasses classes) {
        priority(Priority.HIGH).classes()
                .that().resideInAPackage(SERVICES_PACKAGES)
                .should().notBeAnnotatedWith(MemberPermissionRequired.class)
                .andShould(notHaveMethodAnnotatedWith(MemberPermissionRequired.class))
                .check(classes);
    }

    @ArchTest
    public static void serviceLayerShouldNotBeSecuredWithPermissionServiceAssertion(
            JavaClasses classes) {
        priority(Priority.HIGH).noClasses().that(areInAServicePackagesExcludingPermissions())
                .should().accessClassesThat().resideInAPackage("org.jbb.permissions.(*)")
                .check(classes);
    }

    @ArchTest
    public static void publicMethodsOfRestControllersShouldHaveDefinedErrorInfoCodes(
            JavaClasses classes) {
        priority(Priority.MEDIUM).classes().that().areAnnotatedWith(RestController.class)
                .should(havePublicMethodAnnotatedWith(ErrorInfoCodes.class))
                .check(classes);
    }

    @ArchTest
    public static void publicMethodsOfRestControllersShouldHaveApiOperation(
            JavaClasses classes) {
        priority(Priority.MEDIUM).classes().that().areAnnotatedWith(RestController.class)
                .should(havePublicMethodAnnotatedWith(ApiOperation.class))
                .check(classes);
    }


    private static DescribedPredicate<JavaClass> areInAServicePackagesExcludingPermissions() {
        return new DescribedPredicate<JavaClass>(
                "Service layer (excluding permission service layer)") {
            @Override
            public boolean apply(JavaClass javaClass) {
                return PackageMatcher.of(SERVICES_PACKAGES).matches(javaClass.getPackage()) &&
                        !javaClass.getPackage().startsWith("org.jbb.permissions");
            }
        };
    }

    private static DescribedPredicate<JavaClass> areEntity() {
        return new DescribedPredicate<JavaClass>("Entity class") {
            @Override
            public boolean apply(JavaClass javaClass) {
                return javaClass.isAnnotatedWith(Entity.class) ||
                        javaClass.isAnnotatedWith(Table.class);
            }
        };
    }

    private static ArchCondition<JavaClass> notHaveMethodAnnotatedWith(
            Class<? extends Annotation> annotation) {
        return new ArchCondition<JavaClass>(
                "not have method annotated with @" + annotation.getName()) {
            @Override
            public void check(JavaClass javaClass, ConditionEvents conditionEvents) {
                javaClass.getMethods().stream()
                        .filter(method -> method.isAnnotatedWith(annotation))
                        .forEach(method -> {
                            conditionEvents.add(new SimpleConditionEvent(javaClass, false,
                                    "method " + method.getFullName() + " is annotated with @" + annotation
                                            .getSimpleName()));
                        });
            }
        };
    }

    private static ArchCondition<JavaClass> havePublicMethodAnnotatedWith(
            Class<? extends Annotation> annotation) {
        return new ArchCondition<JavaClass>(
                "have public method annotated with @" + annotation.getName()) {
            @Override
            public void check(JavaClass javaClass, ConditionEvents conditionEvents) {
                javaClass.getMethods().stream()
                        .filter(method -> method.getModifiers().contains(JavaModifier.PUBLIC))
                        .filter(method -> !method.isAnnotatedWith(annotation))
                        .forEach(method -> {
                            conditionEvents.add(new SimpleConditionEvent(javaClass, false,
                                    "method " + method.getFullName() + " is not annotated with @" + annotation
                                            .getSimpleName()));
                        });
            }
        };
    }

    private static DescribedPredicate<JavaClass> notBe(Class clazz) {
        return new DescribedPredicate<JavaClass>("Class " + clazz.getCanonicalName()) {
            @Override
            public boolean apply(JavaClass javaClass) {
                return !javaClass.getName().equals(clazz.getName());
            }
        };
    }
}
