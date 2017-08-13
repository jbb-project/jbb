package org.jbb.lib.db.provider;


public interface DatabaseProvider {

    String getDriverClass();

    String getJdbcUrl();

    String getUsername();

    String getPassword();

}
