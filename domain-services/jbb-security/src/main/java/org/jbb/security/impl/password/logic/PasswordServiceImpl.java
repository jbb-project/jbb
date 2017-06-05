/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password.logic;

import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;

import org.apache.commons.lang3.Validate;
import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.members.event.MemberRemovedEvent;
import org.jbb.security.api.password.PasswordRequirements;
import org.jbb.security.api.password.PasswordException;
import org.jbb.security.api.password.PasswordService;
import org.jbb.security.event.PasswordChangedEvent;
import org.jbb.security.impl.password.dao.PasswordRepository;
import org.jbb.security.impl.password.data.NewPassword;
import org.jbb.security.impl.password.model.PasswordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PasswordServiceImpl implements PasswordService {
    private final PasswordRepository passwordRepository;
    private final PasswordEntityFactory passwordEntityFactory;
    private final PasswordEqualsPolicy passwordEqualsPolicy;
    private final PasswordRequirementsPolicy requirementsPolicy;
    private final Validator validator;
    private final JbbEventBus eventBus;

    @Autowired
    public PasswordServiceImpl(PasswordRepository passwordRepository,
                               PasswordEntityFactory passwordEntityFactory,
                               PasswordEqualsPolicy passwordEqualsPolicy,
                               PasswordRequirementsPolicy requirementsPolicy,
                               Validator validator, JbbEventBus eventBus) {
        this.passwordRepository = passwordRepository;
        this.passwordEntityFactory = passwordEntityFactory;
        this.passwordEqualsPolicy = passwordEqualsPolicy;
        this.requirementsPolicy = requirementsPolicy;
        this.validator = validator;
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    @Transactional
    public void removePasswordEntity(MemberRemovedEvent event) {
        log.debug("Remove password entity for member id {}", event.getMemberId());
        passwordRepository.removeByMemberId(event.getMemberId());
    }

    @Override
    @Transactional
    public void changeFor(Long memberId, Password newPassword) {
        Validate.notNull(memberId, "Member id cannot be null");
        Validate.notNull(newPassword, "Password cannot be null");

        PasswordEntity passwordEntity = passwordEntityFactory.create(memberId, newPassword);

        Set<ConstraintViolation<NewPassword>> passwordValidationResult = validator.validate(
                new NewPassword(String.valueOf(newPassword.getValue()))
        );
        Set<ConstraintViolation<PasswordEntity>> entityValidationResult = validator.validate(passwordEntity);

        if (!passwordValidationResult.isEmpty() || !entityValidationResult.isEmpty()) {
            Set<ConstraintViolation<?>> validationResult = Sets.newHashSet();
            validationResult.addAll(passwordValidationResult);
            validationResult.addAll(entityValidationResult);
            throw new PasswordException(validationResult);
        }

        passwordRepository.save(passwordEntity);

        publishEvent(passwordEntity);
    }

    private void publishEvent(PasswordEntity password) {
        eventBus.post(new PasswordChangedEvent(password.getMemberId()));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verifyFor(Long memberId, Password typedPassword) {
        Validate.notNull(memberId, "Member id cannot be null");
        Validate.notNull(typedPassword, "Password cannot be null");

        Optional<PasswordEntity> currentPasswordEntity = passwordRepository.findTheNewestByMemberId(memberId);
        if (currentPasswordEntity.isPresent()) {
            return passwordEqualsPolicy
                    .matches(typedPassword, currentPasswordEntity.get().getPasswordValueObject());
        }
        return false;
    }

    @Override
    public PasswordRequirements currentRequirements() {
        return requirementsPolicy.currentRequirements();
    }

    @Override
    public void updateRequirements(PasswordRequirements requirements) {
        Validate.notNull(requirements);
        requirementsPolicy.update(requirements);
    }

}
