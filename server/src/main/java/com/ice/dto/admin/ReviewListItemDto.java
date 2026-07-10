package com.ice.dto.admin;

import java.time.Instant;

public record ReviewListItemDto(
        long id,
        long article_id,
        String title,
        int review_type,
        int priority,
        Instant created_at
) {
}
