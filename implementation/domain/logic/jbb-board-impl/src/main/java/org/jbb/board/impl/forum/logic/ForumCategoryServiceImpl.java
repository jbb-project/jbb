/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.impl.forum.logic;

import org.apache.commons.lang3.Validate;
import org.jbb.board.api.exception.ForumCategoryException;
import org.jbb.board.api.model.Forum;
import org.jbb.board.api.model.ForumCategory;
import org.jbb.board.api.service.ForumCategoryService;
import org.jbb.board.event.ForumRemovedEvent;
import org.jbb.board.impl.forum.dao.ForumCategoryRepository;
import org.jbb.board.impl.forum.dao.ForumRepository;
import org.jbb.board.impl.forum.model.ForumCategoryEntity;
import org.jbb.board.impl.forum.model.ForumEntity;
import org.jbb.lib.eventbus.JbbEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

@Service
public class ForumCategoryServiceImpl implements ForumCategoryService {
    private final ForumRepository forumRepository;
    private final ForumCategoryRepository categoryRepository;
    private final JbbEventBus eventBus;
    private final Validator validator;

    @Autowired
    public ForumCategoryServiceImpl(ForumRepository forumRepository, ForumCategoryRepository categoryRepository,
                                    JbbEventBus eventBus, Validator validator) {
        this.forumRepository = forumRepository;
        this.categoryRepository = categoryRepository;
        this.eventBus = eventBus;
        this.validator = validator;
    }

    @Override
    @Transactional
    public ForumCategory addCategory(ForumCategory forumCategory) {
        Validate.notNull(forumCategory);

        Integer lastPosition = getLastCategoryPosition();
        ForumCategoryEntity entity = ForumCategoryEntity.builder()
                .name(forumCategory.getName())
                .position(lastPosition + 1)
                .build();

        Set<ConstraintViolation<ForumCategoryEntity>> validationResult = validator.validate(entity);

        if (!validationResult.isEmpty()) {
            throw new ForumCategoryException(validationResult);
        }

        return categoryRepository.save(entity);
    }

    @Override
    @Transactional
    public ForumCategory moveCategoryToPosition(ForumCategory forumCategory, Integer newPosition) {
        Validate.notNull(forumCategory);
        Validate.notNull(newPosition);
        Validate.inclusiveBetween(1L, getLastCategoryPosition(), newPosition);

        ForumCategoryEntity movingCategoryEntity = categoryRepository.findOne(forumCategory.getId());
        Integer oldPosition = movingCategoryEntity.getPosition();
        List<ForumCategoryEntity> allCategories = categoryRepository.findAllByOrderByPositionAsc();
        allCategories.stream()
                .filter(categoryEntity -> categoryEntity.getId().equals(movingCategoryEntity.getId()))
                .forEach(movedCategoryEntity -> movedCategoryEntity.setPosition(-1));

        allCategories.stream()
                .filter(categoryEntity -> categoryEntity.getPosition() > oldPosition)
                .forEach(entity -> entity.setPosition(entity.getPosition() - 1));

        allCategories.stream()
                .filter(categoryEntity -> categoryEntity.getPosition() >= newPosition)
                .forEach(entity -> entity.setPosition(entity.getPosition() + 1));

        allCategories.stream()
                .filter(categoryEntity -> categoryEntity.getId().equals(movingCategoryEntity.getId()))
                .forEach(movedCategoryEntity -> movedCategoryEntity.setPosition(newPosition));

        categoryRepository.save(allCategories);

        return categoryRepository.findOne(forumCategory.getId());
    }

    @Override
    @Transactional
    public ForumCategory editCategory(ForumCategory forumCategory) {
        Validate.notNull(forumCategory);

        ForumCategoryEntity categoryEntity = categoryRepository.findOne(forumCategory.getId());
        categoryEntity.setName(forumCategory.getName());

        Set<ConstraintViolation<ForumCategoryEntity>> validationResult = validator.validate(categoryEntity);

        if (!validationResult.isEmpty()) {
            throw new ForumCategoryException(validationResult);
        }

        return categoryRepository.save(categoryEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ForumCategory> getCategory(Long id) {
        Validate.notNull(id);
        return Optional.ofNullable(categoryRepository.findOne(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ForumCategory getCategoryWithForum(Forum forum) {
        Validate.notNull(forum);
        return forumRepository.findOne(forum.getId()).getCategory();
    }

    @Override
    @Transactional
    public void removeCategoryAndForums(Long categoryId) {
        Validate.notNull(categoryId);

        ForumCategoryEntity categoryEntityToRemove = categoryRepository.findOne(categoryId);
        Integer removingPosition = categoryEntityToRemove.getPosition();
        List<ForumCategoryEntity> allCategories = categoryRepository.findAllByOrderByPositionAsc();
        allCategories.stream()
                .filter(categoryEntity -> categoryEntity.getPosition() > removingPosition)
                .forEach(categoryEntity -> categoryEntity.setPosition(categoryEntity.getPosition() - 1));
        categoryEntityToRemove.getForums()
                .forEach(forum -> eventBus.post(new ForumRemovedEvent(forum.getId())));
        allCategories.remove(categoryEntityToRemove);
        categoryRepository.save(allCategories);
        categoryRepository.delete(categoryEntityToRemove);
    }

    @Override
    @Transactional
    public void removeCategoryAndMoveForums(Long categoryId, Long newCategoryId) {
        Validate.notNull(categoryId);
        Validate.notNull(newCategoryId);

        ForumCategoryEntity categoryEntityToRemove = categoryRepository.findOne(categoryId);
        Integer removingPosition = categoryEntityToRemove.getPosition();
        List<ForumCategoryEntity> allCategories = categoryRepository.findAllByOrderByPositionAsc();
        allCategories.stream()
                .filter(categoryEntity -> categoryEntity.getPosition() > removingPosition)
                .forEach(categoryEntity -> categoryEntity.setPosition(categoryEntity.getPosition() - 1));
        allCategories.remove(categoryEntityToRemove);
        categoryRepository.save(allCategories);

        ForumCategoryEntity newCategoryEntity = categoryRepository.findOne(newCategoryId);
        int maxForumPosition = newCategoryEntity.getForums().size();

        List<Forum> forumsToMove = categoryEntityToRemove.getForums();
        forumsToMove
                .forEach(forum -> {
                    ((ForumEntity) forum).setCategory(newCategoryEntity);
                    newCategoryEntity.getForumEntities().add((ForumEntity) forum);
                });

        for (int i = 1; i <= forumsToMove.size(); i++) {
            ((ForumEntity) forumsToMove.get(i - 1)).setPosition(maxForumPosition + i);
        }

        categoryRepository.save(newCategoryEntity);
        categoryRepository.delete(categoryEntityToRemove);
    }

    private Integer getLastCategoryPosition() {
        Optional<ForumCategoryEntity> lastCategory = categoryRepository.findTopByOrderByPositionDesc();
        return lastCategory.map(ForumCategoryEntity::getPosition).orElse(0);
    }

}
