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
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @NotNull
    private Long id;

    @NotNull
    private PostAuthor author;

    @NotNull
    private Long topicId;

    @NotBlank
    private String subject;

    @NotNull
    private LocalDateTime postedAt;

    @Builder.Default
    private Optional<LocalDateTime> lastEditedAt = Optional.empty();

    public LocalDateTime lastActionAt() {
        return lastEditedAt.orElse(postedAt);
    }

}
