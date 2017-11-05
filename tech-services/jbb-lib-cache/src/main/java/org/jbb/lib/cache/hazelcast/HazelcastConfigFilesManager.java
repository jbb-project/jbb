/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.cache.hazelcast;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.FileSystemXmlConfig;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.jbb.lib.commons.JbbMetaData;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

@Component
@RequiredArgsConstructor
public class HazelcastConfigFilesManager {
    private static final String HAZELCAST_CLIENT_CONFIG_NAME = "hazelcast-client.xml";
    private static final String HAZELCAST_SERVER_CONFIG_NAME = "hazelcast.xml";
    private static final String HAZELCAST_INTERNAL_CONFIG_NAME = "hazelcast-common-internal.xml";

    private final JbbMetaData jbbMetaData;
    private final HazelcastXmlEditor hazelcastXmlEditor;

    @PostConstruct
    public void putDefaultHazelcastConfigsIfNeeded() {
        copyFromClasspath(HAZELCAST_CLIENT_CONFIG_NAME);
        copyFromClasspath(HAZELCAST_SERVER_CONFIG_NAME);
    }

    public Config getHazelcastServerConfig() {
        try {
            ClasspathXmlConfig internalConfig = new ClasspathXmlConfig(HAZELCAST_INTERNAL_CONFIG_NAME);
            FileSystemXmlConfig userConfig = new FileSystemXmlConfig(
                getHazelcastServerConfigFilename());
            internalConfig.setNetworkConfig(userConfig.getNetworkConfig());
            internalConfig.setGroupConfig(userConfig.getGroupConfig());
            internalConfig.setManagementCenterConfig(userConfig.getManagementCenterConfig());
            return internalConfig;
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public void setHazelcastServerConfig(Config newConfig) {
        Document doc = hazelcastXmlEditor.getXmlConfig(getHazelcastServerConfigFilename());
        hazelcastXmlEditor.updateGroupName(doc, newConfig.getGroupConfig().getName());
        hazelcastXmlEditor.updateGroupPassword(doc, newConfig.getGroupConfig().getPassword());
        hazelcastXmlEditor.updatePort(doc, newConfig.getNetworkConfig().getPort());
        hazelcastXmlEditor.putMemberList(doc,
            newConfig.getNetworkConfig().getJoin().getTcpIpConfig().getMembers());
        hazelcastXmlEditor
            .setManagementCenterEnabled(doc, newConfig.getManagementCenterConfig().isEnabled());
        hazelcastXmlEditor
            .setManagementCenterUrl(doc, newConfig.getManagementCenterConfig().getUrl());
        hazelcastXmlEditor.save(doc, getHazelcastServerConfigFilename());
    }

    public ClientConfig getHazelcastClientConfig() {
        try {
            return new XmlClientConfigBuilder(getHazelcastClientConfigFilename()).build();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void setHazelcastClientConfig(ClientConfig newClientConfig) {
        Document doc = hazelcastXmlEditor.getXmlConfig(getHazelcastClientConfigFilename());
        hazelcastXmlEditor.updateClientGroupName(doc, newClientConfig.getGroupConfig().getName());
        hazelcastXmlEditor
            .updateClientGroupPassword(doc, newClientConfig.getGroupConfig().getPassword());
        hazelcastXmlEditor
            .putClientMemberList(doc, newClientConfig.getNetworkConfig().getAddresses());
        hazelcastXmlEditor.updateClientConnectionAttemptLimit(doc,
            newClientConfig.getNetworkConfig().getConnectionAttemptLimit());
        hazelcastXmlEditor.updateClientConnectionAttemptPeriod(doc,
            newClientConfig.getNetworkConfig().getConnectionAttemptPeriod());
        hazelcastXmlEditor.updateClientConnectionTimeout(doc,
            newClientConfig.getNetworkConfig().getConnectionTimeout());
        hazelcastXmlEditor.save(doc, getHazelcastClientConfigFilename());
    }

    private String getHazelcastServerConfigFilename() {
        return jbbMetaData.jbbHomePath() + File.separator + HAZELCAST_SERVER_CONFIG_NAME;
    }

    private String getHazelcastClientConfigFilename() {
        return jbbMetaData.jbbHomePath() + File.separator + HAZELCAST_CLIENT_CONFIG_NAME;
    }

    private void copyFromClasspath(String classpathFileName) {

        ClassPathResource classPathResource = new ClassPathResource(classpathFileName);
        File targetFile = new File(jbbMetaData.jbbHomePath() + File.separator + classpathFileName);
        try {
            if (!targetFile.exists()) {
                FileUtils.copyURLToFile(classPathResource.getURL(), targetFile);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
