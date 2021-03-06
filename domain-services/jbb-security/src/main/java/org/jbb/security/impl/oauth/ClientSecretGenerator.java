/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.oauth;

import org.apache.commons.lang3.RandomUtils;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Component;

@Component
public class ClientSecretGenerator {
    private final PasswordGenerator passwordGenerator = new PasswordGenerator();

    public String generateSecret() {
        return passwordGenerator.generatePassword(RandomUtils.nextInt(16, 33),
                new CharacterRule(EnglishCharacterData.Alphabetical),
                new CharacterRule(EnglishCharacterData.Digit),
                new CharacterRule(new CharacterData() {
                    @Override
                    public String getErrorCode() {
                        return "INSUFFICIENT_ASCII_SYMBOLS";
                    }

                    @Override
                    public String getCharacters() {
                        return "!#$%&()*+-<=>?@[]^_{}";
                    }
                })
        );
    }

}
