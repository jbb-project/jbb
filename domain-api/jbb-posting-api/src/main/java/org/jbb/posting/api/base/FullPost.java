/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.api.base;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FullPost extends Post {

    @NotBlank
    private String content;

    @Builder(builderMethodName = "fullBuilder")
    public FullPost(Long id, PostAuthor author, Long topicId, String subject,
        LocalDateTime postedAt,
        Optional<LocalDateTime> lastEditedAt, String content) {
        super(id, author, topicId, subject, postedAt, lastEditedAt);
        this.content = content;
    }
}
