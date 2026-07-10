package com.ice.dto.article;

import java.time.Instant;

public record PublishResponse(
        long id,
        String status,
        Instant published_at,
        String reject_reason,
        String message
) {
}
