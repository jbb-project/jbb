/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity;

import com.beust.jcommander.internal.Lists;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;

import org.apache.commons.lang3.Validate;
import org.junit.After;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.List;

@RunWith(SerenityRunner.class)
public abstract class JbbBaseSerenityStories {

    @Managed(uniqueSession = true)
    protected WebDriver driver;

    private List<RollbackAction> rollbackActions = Lists.newLinkedList();

    public void make_rollback_after_test_case(RollbackAction... rollbackAction) {
        Validate.notEmpty(rollbackAction, "Must provide at least one rollback action!");
        this.rollbackActions.clear();
        this.rollbackActions.addAll(Arrays.asList(rollbackAction));
    }

    public void no_rollback_after_test_case() {
        rollbackActions.clear();
    }

    @After
    public void tearDown() throws Exception {
        rollbackActions.forEach(RollbackAction::rollback);
    }

    @FunctionalInterface
    public interface RollbackAction {
        void rollback();
    }
}
