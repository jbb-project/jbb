package org.jbb.system.impl.database.logic;

import lombok.RequiredArgsConstructor;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.impl.database.logic.provider.DatabaseProvidersService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSettingsSaver {

    private final CommonDatabaseSettingsManager commonSettingsManager;
    private final DatabaseProvidersService databaseProvidersService;

    public void setDatabaseSettings(DatabaseSettings newDatabaseSettings) {
        commonSettingsManager.setCommonSettings(newDatabaseSettings);
        databaseProvidersService.setSettingsForAllProviders(newDatabaseSettings);
        databaseProvidersService.setNewProvider(newDatabaseSettings);
    }

}
