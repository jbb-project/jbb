/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchRules;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;

import org.junit.runner.RunWith;

import static org.jbb.webapp.architecture.WebAppArchTesting.JBB_ROOT_PACKAGE;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = JBB_ROOT_PACKAGE, importOptions = ImportOption.DontIncludeTests.class)
public class WebAppArchTesting {
    public static final String JBB_ROOT_PACKAGE = "org.jbb";

    @ArchTest
    public static final ArchRules commonJbbArchitectureRules = ArchRules.in(JbbArchRules.class);
}
