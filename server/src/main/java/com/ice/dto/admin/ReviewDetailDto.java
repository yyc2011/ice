package com.ice.dto.admin;

import java.time.Instant;

public record ReviewDetailDto(
        long id,
        long article_id,
        String title,
        String content,
        Integer review_type,
        String appeal_text,
        Integer ai_score,
        String ai_dimensions,
        Instant created_at
) {
}
