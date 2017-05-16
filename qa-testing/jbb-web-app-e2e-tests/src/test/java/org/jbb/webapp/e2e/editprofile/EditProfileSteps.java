/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp.e2e.editprofile;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

public class EditProfileSteps extends ScenarioSteps {
    UcpEditProfilePage editProfilePage;

    @Step
    public void type_displayed_name(String displayedName) {
        editProfilePage.typeDisplayedName(displayedName);
    }

    @Step
    public void send_edit_profile_form() {
        editProfilePage.sendForm();
    }

    @Step
    public void should_be_informed_about_incorrect_display_name_length() {
        editProfilePage.containsInfoAboutIncorrectDisplayedNameLength();
    }

    @Step
    public void should_be_informed_about_saving_settings() {
        editProfilePage.containsInfoAboutSavingSettingsCorrectly();
    }

    @Step
    public void current_displayed_name_should_be_visible_as_link_to_ucp(String displayedName) {
        editProfilePage.containsDisplayedNameAsLinkToUcp(displayedName);
    }
}
