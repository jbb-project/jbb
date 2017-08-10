package org.jbb.system.impl.database.logic.provider;

import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseProviderSettings;
import org.jbb.system.api.database.DatabaseSettings;

public interface DatabaseProviderManager<T extends DatabaseProviderSettings> {

    DatabaseProvider getProviderName();

    T getCurrentProviderSettings();

    void setProviderSettings(DatabaseSettings newDatabaseSettings);

    default void setAsCurrentProvider(DatabaseSettings newDatabaseSettings) {
        newDatabaseSettings.setCurrentDatabaseProvider(getProviderName());
    }

}
