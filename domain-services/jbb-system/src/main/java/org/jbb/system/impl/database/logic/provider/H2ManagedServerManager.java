package org.jbb.system.impl.database.logic.provider;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.db.DbProperties;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.h2.H2ConnectionType;
import org.jbb.system.api.database.h2.H2EncryptionAlgorithm;
import org.jbb.system.api.database.h2.H2ManagedServerSettings;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class H2ManagedServerManager extends DatabaseProviderManager<H2ManagedServerSettings> {

    public static final String PROVIDER_PROPERTY_VALUE = "h2-managed-server";

    private final DbProperties dbProperties;

    @Override
    public DatabaseProvider getProviderName() {
        return DatabaseProvider.H2_MANAGED_SERVER;
    }

    @Override
    public H2ManagedServerSettings getCurrentProviderSettings() {
        return H2ManagedServerSettings.builder()
            .databaseFileName(dbProperties.h2ManagedServerDbName())
            .port(Integer.valueOf(dbProperties.h2ManagedServerDbPort()))
            .username(dbProperties.h2ManagedServerUsername())
            .usernamePassword(dbProperties.h2ManagedServerPassword())
            .filePassword(dbProperties.h2ManagedServerFilePassword())
            .connectionType(H2ConnectionType
                .valueOf(dbProperties.h2ManagedServerConnectionType().toUpperCase()))
            .encryptionAlgorithm(
                StringUtils.isEmpty(dbProperties.h2ManagedServerDbEncryptionAlgorithm()) ?
                    Optional.empty() : Optional.of(H2EncryptionAlgorithm
                    .valueOf(dbProperties.h2ManagedServerDbEncryptionAlgorithm().toUpperCase())))
            .build();
    }

    @Override
    public void setProviderSettings(DatabaseSettings newDatabaseSettings) {
        H2ManagedServerSettings newProviderSettings = newDatabaseSettings
            .getH2ManagedServerSettings();

        dbProperties.setProperty(DbProperties.H2_MANAGED_SERVER_DB_NAME_KEY,
            newProviderSettings.getDatabaseFileName());
        dbProperties.setProperty(DbProperties.H2_MANAGED_SERVER_DB_PORT_KEY,
            Integer.toString(newProviderSettings.getPort()));
        dbProperties.setProperty(DbProperties.H2_MANAGED_SERVER_DB_USERNAME_KEY,
            newProviderSettings.getUsername());
        dbProperties.setProperty(DbProperties.H2_MANAGED_SERVER_DB_PASS_KEY,
            newProviderSettings.getUsernamePassword());
        dbProperties.setProperty(DbProperties.H2_MANAGED_SERVER_DB_FILE_PASS_KEY,
            newProviderSettings.getFilePassword());
        dbProperties.setProperty(DbProperties.H2_MANAGED_SERVER_DB_CONNECTION_TYPE_KEY,
            newProviderSettings.getConnectionType().toString().toLowerCase());
        dbProperties.setProperty(DbProperties.H2_MANAGED_SERVER_DB_ENCRYPTION_ALGORITHM_KEY,
            newProviderSettings.getEncryptionAlgorithm().isPresent() ?
                newProviderSettings.getEncryptionAlgorithm().get().toString().toLowerCase() :
                StringUtils.EMPTY);
    }

}
