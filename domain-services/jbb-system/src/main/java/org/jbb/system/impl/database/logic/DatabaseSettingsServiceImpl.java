package org.jbb.system.impl.database.logic;

import java.util.Set;
import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.jbb.lib.db.DbProperties;
import org.jbb.system.api.database.CommonDatabaseSettings;
import org.jbb.system.api.database.DatabaseConfigException;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.DatabaseSettingsService;
import org.jbb.system.api.database.h2.H2ManagedServerSettings;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseSettingsServiceImpl implements DatabaseSettingsService {
    private final DbProperties dbProperties;
    private final DatabaseSettingsFactory databaseSettingsFactory;
    private final Validator validator;
    private final ReconnectionToDbPropertyListener reconnectionPropertyListener;
    private final ConnectionToDatabaseEventSender eventSender;

    @PostConstruct
    public void addReconnectionPropertyListenerToDbProperties() {
        dbProperties.addPropertyChangeListener(reconnectionPropertyListener);
    }

    @Override
    public DatabaseSettings getDatabaseSettings() {
        return databaseSettingsFactory.currentDatabaseSettings();
    }

    @Override
    public void setDatabaseSettings(DatabaseSettings newDatabaseSettings) {
        Validate.notNull(newDatabaseSettings);

        Set<ConstraintViolation<DatabaseSettings>> validationResult = validator.validate(newDatabaseSettings);
        if (!validationResult.isEmpty()) {
            throw new DatabaseConfigException(validationResult);
        }

        dbProperties.removePropertyChangeListener(reconnectionPropertyListener);

        CommonDatabaseSettings newCommonSettings = newDatabaseSettings.getCommonSettings();
        dbProperties.setProperty(DbProperties.DB_MIN_IDLE_KEY,
            Integer.toString(newCommonSettings.getMinimumIdleConnections()));
        dbProperties.setProperty(DbProperties.DB_MAX_POOL_KEY,
            Integer.toString(newCommonSettings.getMaximumPoolSize()));
        dbProperties.setProperty(DbProperties.DB_CONN_TIMEOUT_MS_KEY,
            Integer.toString(newCommonSettings.getConnectionTimeoutMilliseconds()));
        dbProperties.setProperty(DbProperties.DB_CONN_MAX_LIFETIME_MS_KEY,
            Integer.toString(newCommonSettings.getConnectionMaxLifetimeMilliseconds()));
        dbProperties.setProperty(DbProperties.DB_IDLE_TIMEOUT_MS_KEY,
            Integer.toString(newCommonSettings.getIdleTimeoutMilliseconds()));
        dbProperties.setProperty(DbProperties.DB_VALIDATION_TIMEOUT_MS_KEY,
            Integer.toString(newCommonSettings.getValidationTimeoutMilliseconds()));
        dbProperties.setProperty(DbProperties.DB_LEAK_DETECTION_THRESHOLD_MS_KEY,
            Integer.toString(newCommonSettings.getLeakDetectionThresholdMilliseconds()));
        dbProperties.setProperty(DbProperties.DB_INIT_FAIL_FAST_KEY,
            Boolean.toString(newCommonSettings.isFailAtStartingImmediately()));
        dbProperties.setProperty(DbProperties.DB_DROP_DURING_START_KEY,
            Boolean.toString(newCommonSettings.isDropDatabaseAtStart()));
        dbProperties.setProperty(DbProperties.DB_AUDIT_ENABLED_KEY,
            Boolean.toString(newCommonSettings.isAuditEnabled()));

        H2ManagedServerSettings newProviderSettings = (H2ManagedServerSettings) newDatabaseSettings
            .getProviderSettings();
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
            newProviderSettings.getEncryptionAlgorithm().toString().toLowerCase());

        dbProperties.addPropertyChangeListener(reconnectionPropertyListener);

        eventSender.emitEvent();

    }
}
