/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.impl.base;

import com.github.zafarkhaja.semver.Version;
import lombok.RequiredArgsConstructor;
import org.jbb.board.api.base.BoardSettings;
import org.jbb.board.api.base.BoardSettingsService;
import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.install.JbbVersions;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardInstallAction implements InstallUpdateAction {

    private final BoardSettingsService boardSettingsService;

    @Override
    public Version fromVersion() {
        return JbbVersions.VERSION_0_4_0;
    }

    @Override
    public void install(InstallationData installationData) {
        BoardSettings boardSettings = boardSettingsService.getBoardSettings();
        boardSettings.setBoardName(installationData.getBoardName());
        boardSettingsService.setBoardSettings(boardSettings);
    }
}
