/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp.e2e;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;

import org.junit.After;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

@RunWith(SerenityRunner.class)
public abstract class Jbb_Base_Stories {

    @Managed(uniqueSession = true)
    protected WebDriver driver;

    private Optional<RollbackAction> rollbackAction = Optional.empty();

    public void makeRollbackAfterTestCase(RollbackAction rollbackAction) {
        this.rollbackAction = Optional.of(rollbackAction);
    }

    public void skipRollbackAfterTestCase() {
        rollbackAction = Optional.empty();
    }

    @After
    public void tearDown() throws Exception {
        rollbackAction.ifPresent(RollbackAction::rollback);
    }

    @FunctionalInterface
    public interface RollbackAction {
        void rollback();
    }
}
