/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.cache.logic;

import org.apache.commons.lang3.StringUtils;
import org.jbb.system.api.cache.CacheProvider;
import org.jbb.system.api.cache.CacheSettings;
import org.jbb.system.api.cache.HazelcastClientSettings;
import org.jbb.system.api.cache.HazelcastServerSettings;
import org.jbb.system.web.cache.form.CacheSettingsForm;
import org.jbb.system.web.cache.form.HazelcastClientSettingsForm;
import org.jbb.system.web.cache.form.HazelcastServerSettingsForm;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static org.jbb.system.api.cache.CacheUtils.buildHazelcastMemberList;

@Component
public class FormCacheTranslator {

    public CacheSettingsForm fillCacheSettingsForm(CacheSettings cacheSettings,
                                                   CacheSettingsForm form) {

        form.setApplicationCacheEnabled(cacheSettings.isApplicationCacheEnabled());
        form.setSecondLevelCacheEnabled(cacheSettings.isSecondLevelCacheEnabled());
        form.setQueryCacheEnabled(cacheSettings.isQueryCacheEnabled());
        form.setProviderName(cacheSettings.getCurrentCacheProvider().toString());

        HazelcastServerSettings hazelcastServerSettings = cacheSettings
                .getHazelcastServerSettings();
        HazelcastServerSettingsForm hazelcastServerForm = form.getHazelcastServerSettings();
        hazelcastServerForm.setGroupName(hazelcastServerSettings.getGroupName());
        hazelcastServerForm.setMembers(String.join(", ", hazelcastServerSettings.getMembers()));
        hazelcastServerForm.setServerPort(hazelcastServerSettings.getServerPort());
        hazelcastServerForm
                .setManagementCenterEnabled(hazelcastServerSettings.isManagementCenterEnabled());
        hazelcastServerForm
                .setManagementCenterUrl(hazelcastServerSettings.getManagementCenterUrl());

        HazelcastClientSettings hazelcastClientSettings = cacheSettings
                .getHazelcastClientSettings();
        HazelcastClientSettingsForm hazelcastClientForm = form.getHazelcastClientSettings();
        hazelcastClientForm.setGroupName(hazelcastClientSettings.getGroupName());
        hazelcastClientForm.setMembers(String.join(", ", hazelcastClientSettings.getMembers()));
        hazelcastClientForm
                .setConnectionAttemptLimit(hazelcastClientSettings.getConnectionAttemptLimit());
        hazelcastClientForm.setConnectionAttemptPeriod(
                Math.toIntExact(hazelcastClientSettings.getConnectionAttemptPeriod().toMillis()));
        hazelcastClientForm.setConnectionTimeout(
                Math.toIntExact(hazelcastClientSettings.getConnectionTimeout().toMillis()));

        return form;
    }

    public CacheSettings buildCacheSettings(CacheSettingsForm form,
                                            CacheSettings currentCacheSettings) {
        return CacheSettings.builder()
                .applicationCacheEnabled(form.isApplicationCacheEnabled())
                .secondLevelCacheEnabled(form.isSecondLevelCacheEnabled())
                .queryCacheEnabled(form.isQueryCacheEnabled())
                .hazelcastServerSettings(buildHazelcastServerSettings(form, currentCacheSettings))
                .hazelcastClientSettings(buildHazelcastClientSettings(form, currentCacheSettings))
                .currentCacheProvider(CacheProvider.valueOf(form.getProviderName().toUpperCase()))
                .build();
    }

    private HazelcastServerSettings buildHazelcastServerSettings(CacheSettingsForm form,
                                                                 CacheSettings currentCacheSettings) {
        HazelcastServerSettings currentServerSettings = currentCacheSettings
                .getHazelcastServerSettings();
        HazelcastServerSettingsForm newServerSettings = form.getHazelcastServerSettings();
        HazelcastServerSettings serverSettings = new HazelcastServerSettings();
        serverSettings.setGroupName(newServerSettings.getGroupName());
        serverSettings.setGroupPassword(StringUtils.isEmpty(newServerSettings.getGroupPassword()) ?
                currentServerSettings.getGroupPassword() : newServerSettings.getGroupPassword());
        serverSettings.setMembers(buildHazelcastMemberList(newServerSettings.getMembers(), false));
        serverSettings.setServerPort(newServerSettings.getServerPort());
        serverSettings.setManagementCenterEnabled(newServerSettings.isManagementCenterEnabled());
        serverSettings.setManagementCenterUrl(newServerSettings.getManagementCenterUrl());
        return serverSettings;
    }

    private HazelcastClientSettings buildHazelcastClientSettings(CacheSettingsForm form,
                                                                 CacheSettings currentCacheSettings) {
        HazelcastClientSettings currentClientSettings = currentCacheSettings
                .getHazelcastClientSettings();
        HazelcastClientSettingsForm newClientSettings = form.getHazelcastClientSettings();
        HazelcastClientSettings clientSettings = new HazelcastClientSettings();
        clientSettings.setGroupName(newClientSettings.getGroupName());
        clientSettings.setGroupPassword(StringUtils.isEmpty(newClientSettings.getGroupPassword()) ?
                currentClientSettings.getGroupPassword() : newClientSettings.getGroupPassword());
        clientSettings.setMembers(buildHazelcastMemberList(newClientSettings.getMembers(), true));
        clientSettings.setConnectionAttemptLimit(newClientSettings.getConnectionAttemptLimit());
        clientSettings.setConnectionAttemptPeriod(
                Duration.ofMillis(newClientSettings.getConnectionAttemptPeriod()));
        clientSettings
                .setConnectionTimeout(Duration.ofMillis(newClientSettings.getConnectionTimeout()));
        return clientSettings;
    }

}
