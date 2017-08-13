package org.jbb.lib.db.provider;

import org.apache.commons.lang3.StringUtils;

public abstract class H2AbstractProvider implements DatabaseProvider {

    public static final String DB_SUBDIR_NAME = "db";


    @Override
    public String getDriverClass() {
        return "org.h2.Driver"; //NOSONAR
    }

    abstract String getEncryptionAlgorithm();

    String resolveCipher() {
        String encryptionAlgorithm = getEncryptionAlgorithm();
        if (StringUtils.isNotBlank(encryptionAlgorithm)) {
            return "cipher=" + encryptionAlgorithm.toLowerCase();
        } else {
            return StringUtils.EMPTY;
        }
    }

}
