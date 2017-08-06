package org.jbb.system.api.database;

public interface DatabaseSettingsService {
    DatabaseSettings getDatabaseSettings();

    void setDatabaseSettings(DatabaseSettings databaseSettings);
}
