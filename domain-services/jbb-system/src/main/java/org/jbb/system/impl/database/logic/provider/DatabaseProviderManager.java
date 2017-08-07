package org.jbb.system.impl.database.logic.provider;

import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseProviderSettings;
import org.jbb.system.api.database.DatabaseSettings;

public abstract class DatabaseProviderManager<T extends DatabaseProviderSettings> {

    public abstract DatabaseProvider getProviderName();

    public abstract T getCurrentProviderSettings();

    public abstract void setProviderSettings(DatabaseSettings newDatabaseSettings);

    public void setAsCurrentProvider(DatabaseSettings newDatabaseSettings) {
        newDatabaseSettings.setCurrentDatabaseProvider(getProviderName());
    }

}
