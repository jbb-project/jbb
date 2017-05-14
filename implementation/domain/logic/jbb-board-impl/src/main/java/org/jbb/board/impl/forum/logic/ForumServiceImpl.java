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
import org.jbb.board.api.model.Forum;
import org.jbb.board.api.model.ForumCategory;
import org.jbb.board.api.service.ForumService;
import org.jbb.board.event.ForumCreatedEvent;
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

@Service
public class ForumServiceImpl implements ForumService {
    private final ForumRepository forumRepository;
    private final ForumCategoryRepository categoryRepository;
    private final JbbEventBus eventBus;

    @Autowired
    public ForumServiceImpl(ForumRepository forumRepository, ForumCategoryRepository categoryRepository,
                            JbbEventBus eventBus) {
        this.forumRepository = forumRepository;
        this.categoryRepository = categoryRepository;
        this.eventBus = eventBus;
    }

    @Override
    @Transactional(readOnly = true)
    public Forum getForum(Long id) {
        return forumRepository.findOne(id);
    }

    @Override
    @Transactional
    public Forum addForum(Forum forum, ForumCategory category) {
        ForumCategoryEntity categoryEntity = categoryRepository.findOne(category.getId());

        Integer lastPosition = getLastForumPosition(categoryEntity);

        ForumEntity forumEntity = ForumEntity.builder()
                .name(forum.getName())
                .description(forum.getDescription())
                .locked(forum.isLocked())
                .position(lastPosition + 1)
                .category(categoryEntity)
                .build();

        forumEntity = forumRepository.save(forumEntity);

        eventBus.post(new ForumCreatedEvent(forumEntity.getId()));

        return forumEntity;
    }

    @Override
    @Transactional
    public Forum moveForumToPosition(Forum forum, Integer newPosition) {
        ForumEntity movingForumEntity = forumRepository.findOne(forum.getId());
        Integer oldPosition = movingForumEntity.getPosition();
        ForumCategoryEntity categoryEntity = movingForumEntity.getCategory();

        Validate.inclusiveBetween(1L, getLastForumPosition(categoryEntity), newPosition);
        List<ForumEntity> allForums = forumRepository.findAllByCategoryOrderByPositionAsc(categoryEntity);
        allForums.stream()
                .filter(forumEntity -> forumEntity.getId().equals(movingForumEntity.getId()))
                .forEach(movedForumEntity -> movedForumEntity.setPosition(-1));

        allForums.stream()
                .filter(forumEntity -> forumEntity.getPosition() > oldPosition)
                .forEach(entity -> entity.setPosition(entity.getPosition() - 1));

        allForums.stream()
                .filter(forumEntity -> forumEntity.getPosition() >= newPosition)
                .forEach(entity -> entity.setPosition(entity.getPosition() + 1));

        allForums.stream()
                .filter(forumEntity -> forumEntity.getId().equals(movingForumEntity.getId()))
                .forEach(movedForumEntity -> movedForumEntity.setPosition(newPosition));

        forumRepository.save(allForums);

        return forumRepository.findOne(forum.getId());
    }

    @Override
    @Transactional
    public Forum moveForumToAnotherCategory(Long forumId, Long categoryId) {
        ForumEntity movingForumEntity = forumRepository.findOne(forumId);
        ForumCategoryEntity newCategoryEntity = categoryRepository.findOne(categoryId);

        ForumCategoryEntity currentCategoryEntity = movingForumEntity.getCategory();
        currentCategoryEntity.getForumEntities().remove(movingForumEntity);
        movingForumEntity.setCategory(null);

        categoryRepository.save(currentCategoryEntity);

        movingForumEntity.setCategory(newCategoryEntity);
        movingForumEntity.setPosition(getLastForumPosition(newCategoryEntity) + 1);
        newCategoryEntity.getForumEntities().add(movingForumEntity);

        categoryRepository.save(newCategoryEntity);

        return forumRepository.findOne(forumId);
    }

    @Override
    @Transactional
    public Forum editForum(Forum forum) {
        ForumEntity forumEntity = forumRepository.findOne(forum.getId());
        forumEntity.setName(forum.getName());
        forumEntity.setDescription(forum.getDescription());
        forumEntity.setLocked(forum.isLocked());
        return forumRepository.save(forumEntity);
    }

    @Override
    @Transactional
    public void removeForum(Long forumId) {
        ForumEntity forumEntityToRemove = forumRepository.findOne(forumId);
        Integer removingPosition = forumEntityToRemove.getPosition();
        ForumCategoryEntity categoryEntity = forumEntityToRemove.getCategory();
        categoryEntity.getForumEntities().remove(forumEntityToRemove);
        categoryEntity.getForumEntities().stream()
                .filter(forumEntity -> forumEntity.getPosition() > removingPosition)
                .forEach(forumEntity -> forumEntity.setPosition(forumEntity.getPosition() - 1));
        forumRepository.delete(forumId);
        eventBus.post(new ForumRemovedEvent(forumId));
        categoryRepository.save(categoryEntity);
    }

    private Integer getLastForumPosition(ForumCategoryEntity categoryEntity) {
        Optional<ForumEntity> lastForum = forumRepository.findTopByCategoryOrderByPositionDesc(categoryEntity);
        return lastForum.map(ForumEntity::getPosition).orElse(0);
    }
}
