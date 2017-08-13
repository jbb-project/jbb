package org.jbb.lib.db.provider;

import lombok.RequiredArgsConstructor;
import org.jbb.lib.commons.JbbMetaData;
import org.jbb.lib.db.DbProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class H2InMemoryProvider extends H2AbstractProvider {

    public static final String PROVIDER_VALUE = "h2-in-memory";

    private static final String H2_FILE_PREFIX = "jdbc:h2:file:";

    private final JbbMetaData jbbMetaData;
    private final DbProperties dbProperties;

    @Override
    public String getJdbcUrl() {
        return String.format("%s%s/%s/%s;%s",
            H2_FILE_PREFIX,
            jbbMetaData.jbbHomePath(),
            DB_SUBDIR_NAME,
            dbProperties.h2InMemoryDbName(),
            resolveCipher());
    }

    @Override
    public String getUsername() {
        return dbProperties.h2InMemoryUsername();
    }

    @Override
    public String getPassword() {
        if (resolveCipher().isEmpty()) {
            return dbProperties.h2InMemoryPassword();
        } else {
            return dbProperties.h2InMemoryFilePassword() + " " + dbProperties.h2InMemoryPassword();
        }
    }

    @Override
    String getEncryptionAlgorithm() {
        return dbProperties.h2InMemoryDbEncryptionAlgorithm();
    }
}
