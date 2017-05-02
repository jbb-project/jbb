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
import org.jbb.board.api.service.BoardService;
import org.jbb.board.impl.forum.dao.ForumCategoryRepository;
import org.jbb.board.impl.forum.dao.ForumRepository;
import org.jbb.board.impl.forum.model.ForumCategoryEntity;
import org.jbb.board.impl.forum.model.ForumEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {
    private final ForumRepository forumRepository;
    private final ForumCategoryRepository categoryRepository;

    @Autowired
    public BoardServiceImpl(ForumRepository forumRepository, ForumCategoryRepository categoryRepository) {
        this.forumRepository = forumRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ForumCategory> getForumCategories() {
        return categoryRepository.findAllByOrderByOrderingAsc().stream()
                .map(entity -> (ForumCategory) entity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ForumCategory addCategory(ForumCategory forumCategory) {
        Integer lastPosition = getLastCategoryPosition();
        ForumCategoryEntity entity = ForumCategoryEntity.builder()
                .name(forumCategory.getName())
                .ordering(lastPosition + 1)
                .build();

        return categoryRepository.save(entity);
    }

    @Override
    @Transactional
    public ForumCategory moveCategoryToPosition(ForumCategory forumCategory, Integer newPosition) {
        Validate.inclusiveBetween(1L, getLastCategoryPosition(), newPosition);
        ForumCategoryEntity movingCategoryEntity = categoryRepository.findOne(forumCategory.getId());
        Integer oldPosition = movingCategoryEntity.getOrdering();
        List<ForumCategoryEntity> allCategories = categoryRepository.findAllByOrderByOrderingAsc();
        allCategories.stream()
                .filter(categoryEntity -> categoryEntity.getId().equals(movingCategoryEntity.getId()))
                .forEach(movedCategoryEntity -> movedCategoryEntity.setOrdering(-1));

        allCategories.stream()
                .filter(categoryEntity -> categoryEntity.getOrdering() > oldPosition)
                .forEach(entity -> entity.setOrdering(entity.getOrdering() - 1));

        allCategories.stream()
                .filter(categoryEntity -> categoryEntity.getOrdering() >= newPosition)
                .forEach(entity -> entity.setOrdering(entity.getOrdering() + 1));

        allCategories.stream()
                .filter(categoryEntity -> categoryEntity.getId().equals(movingCategoryEntity.getId()))
                .forEach(movedCategoryEntity -> movedCategoryEntity.setOrdering(newPosition));

        categoryRepository.save(allCategories);

        return categoryRepository.findOne(forumCategory.getId());
    }

    @Override
    @Transactional
    public ForumCategory editCategory(ForumCategory forumCategory) {
        ForumCategoryEntity categoryEntity = categoryRepository.findOne(forumCategory.getId());
        categoryEntity.setName(forumCategory.getName());
        return categoryRepository.save(categoryEntity);
    }

    @Override
    @Transactional
    public void removeCategoryAndForums(Long categoryId) {
        ForumCategoryEntity categoryEntityToRemove = categoryRepository.findOne(categoryId);
        Integer removingPosition = categoryEntityToRemove.getOrdering();
        List<ForumCategoryEntity> allCategories = categoryRepository.findAllByOrderByOrderingAsc();
        allCategories.stream()
                .filter(categoryEntity -> categoryEntity.getOrdering() > removingPosition)
                .forEach(categoryEntity -> categoryEntity.setOrdering(categoryEntity.getOrdering() - 1));
        allCategories.remove(categoryEntityToRemove);
        categoryRepository.save(allCategories);
        categoryRepository.delete(categoryEntityToRemove);
    }

    @Override
    @Transactional
    public void removeCategoryAndMoveForums(Long categoryId, Long newCategoryId) {
        ForumCategoryEntity categoryEntityToRemove = categoryRepository.findOne(categoryId);
        Integer removingPosition = categoryEntityToRemove.getOrdering();
        List<ForumCategoryEntity> allCategories = categoryRepository.findAllByOrderByOrderingAsc();
        allCategories.stream()
                .filter(categoryEntity -> categoryEntity.getOrdering() > removingPosition)
                .forEach(categoryEntity -> categoryEntity.setOrdering(categoryEntity.getOrdering() - 1));
        allCategories.remove(categoryEntityToRemove);
        categoryRepository.save(allCategories);

        ForumCategoryEntity newCategoryEntity = categoryRepository.findOne(newCategoryId);

        List<Forum> forumsToMove = categoryEntityToRemove.getForums();
        forumsToMove.stream()
                .forEach(forum -> {
                    ((ForumEntity) forum).setCategory(newCategoryEntity);
                    newCategoryEntity.getForumEntities().add((ForumEntity) forum);
                });

        categoryRepository.save(newCategoryEntity);
        categoryRepository.delete(categoryEntityToRemove);
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
                .ordering(lastPosition + 1)
                .category(categoryEntity)
                .build();

        return forumRepository.save(forumEntity);
    }

    @Override
    @Transactional
    public Forum moveForumToPosition(Forum forum, Integer newPosition) {
        ForumEntity movingForumEntity = forumRepository.findOne(forum.getId());
        Integer oldPosition = movingForumEntity.getOrdering();
        ForumCategoryEntity categoryEntity = movingForumEntity.getCategory();

        Validate.inclusiveBetween(1L, getLastForumPosition(categoryEntity), newPosition);
        List<ForumEntity> allForums = forumRepository.findAllByCategoryOrderByOrderingAsc(categoryEntity);
        allForums.stream()
                .filter(forumEntity -> forumEntity.getId().equals(movingForumEntity.getId()))
                .forEach(movedForumEntity -> movedForumEntity.setOrdering(-1));

        allForums.stream()
                .filter(forumEntity -> forumEntity.getOrdering() > oldPosition)
                .forEach(entity -> entity.setOrdering(entity.getOrdering() - 1));

        allForums.stream()
                .filter(forumEntity -> forumEntity.getOrdering() >= newPosition)
                .forEach(entity -> entity.setOrdering(entity.getOrdering() + 1));

        allForums.stream()
                .filter(forumEntity -> forumEntity.getId().equals(movingForumEntity.getId()))
                .forEach(movedForumEntity -> movedForumEntity.setOrdering(newPosition));

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
        movingForumEntity.setOrdering(getLastForumPosition(newCategoryEntity) + 1);
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
        Integer removingPosition = forumEntityToRemove.getOrdering();
        ForumCategoryEntity categoryEntity = forumEntityToRemove.getCategory();
        categoryEntity.getForumEntities().remove(forumEntityToRemove);
        categoryEntity.getForumEntities().stream()
                .filter(forumEntity -> forumEntity.getOrdering() > removingPosition)
                .forEach(forumEntity -> forumEntity.setOrdering(forumEntity.getOrdering() - 1));
        forumRepository.delete(forumId);
        categoryRepository.save(categoryEntity);
    }

    private Integer getLastCategoryPosition() {
        Optional<ForumCategoryEntity> lastCategory = categoryRepository.findTopByOrderByOrderingDesc();
        if (lastCategory.isPresent()) {
            return lastCategory.get().getOrdering();
        } else {
            return 0;
        }
    }

    private Integer getLastForumPosition(ForumCategoryEntity categoryEntity) {
        Optional<ForumEntity> lastForum = forumRepository.findTopByCategoryOrderByOrderingDesc(categoryEntity);
        if (lastForum.isPresent()) {
            return lastForum.get().getOrdering();
        } else {
            return 0;
        }
    }
}
