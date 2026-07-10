package com.ice.dto.article;

import java.time.Instant;
import java.util.List;

public record ArticleDetailResponse(
        long id,
        String title,
        String content,
        String status,
        String reject_reason,
        Integer ai_quality_score,
        List<TagDto> tags,
        LatestReviewDto latest_review,
        Instant published_at,
        Instant created_at
) {
}
