package com.ice.dto.me;

import java.time.Instant;

public record MyArticleItem(
        long id,
        String title,
        String status,
        Instant updated_at
) {
}
