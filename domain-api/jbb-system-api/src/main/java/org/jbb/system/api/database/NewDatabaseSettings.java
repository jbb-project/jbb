package org.jbb.system.api.database;

import lombok.Getter;
import lombok.Setter;

//FIXME temporary name
@Getter
@Setter
public class NewDatabaseSettings {

    private CommonDatabaseSettings commonSettings;

    private DatabaseProviderSettings providerSettings;

}
