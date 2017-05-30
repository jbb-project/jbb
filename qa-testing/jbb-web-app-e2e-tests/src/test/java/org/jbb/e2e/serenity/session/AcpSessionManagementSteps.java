/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.session;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;


public class AcpSessionManagementSteps extends ScenarioSteps {

    AcpSessionManagementPage acpSessionManagementPage;

    @Step
    public void open_session_management_page(){
        acpSessionManagementPage.open();
    }

    @Step
    public void click_save_button(){
        acpSessionManagementPage.saveButton.click();
    }

    @Step
    public void user_should_save_alert_with_message(String message){
        acpSessionManagementPage.waitForAllTextToAppear().waitForTextToAppear(message,2);
    }

    @Step
    public void user__with_specify_username_should_be_display_on_session_board(String username){
//        acpSessionManagementPage.findAll(By.xpath("/html/body/div[2]/div[1]/div[2]/div[2]/div/table/tbody/tr")).stream()
//                        .map(tableRow -> tableRow.findElements(By.tagName("td")))
//                        .flatMap(tableRowCells -> tableRowCells.stream())
//                        .forEach(td -> td.);
//


    }

    @Step
    public void provide_empty_value_to_text_field(){

    }

    @Step
    public void provide_negative_value_to_text_field(){

    }

    @Step
    public void provide_zero_as_a_value_to_text_field(){

    }
    @Step
    public void type_maximum_inactive_interval(String maximumInactive){
        acpSessionManagementPage.inputForm.click();
        acpSessionManagementPage.inputForm.clear();
        acpSessionManagementPage.inputForm.sendKeys(maximumInactive);
    }


    @Step
    public void create_signed_in_user(){

    }

    @Step
    public void check_if_user_disapear_from_table_after_log_off(){

    }
}
