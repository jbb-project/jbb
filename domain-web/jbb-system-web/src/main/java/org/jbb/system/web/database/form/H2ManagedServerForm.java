package org.jbb.system.web.database.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class H2ManagedServerForm {

    private String databaseFileName;

    private int port;

    private String username;

    private String usernamePassword;

    private String filePassword;

    private String connectionType;

    private String encryptionAlgorithm;

}
