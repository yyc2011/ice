package com.ice.dto.notification;

import java.time.Instant;

public record NotificationItemDto(
        long id,
        String type,
        String title,
        String content,
        String ref_type,
        Long ref_id,
        boolean is_read,
        Instant created_at
) {
}
