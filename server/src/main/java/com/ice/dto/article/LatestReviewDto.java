package com.ice.dto.article;

import java.time.Instant;

public record LatestReviewDto(String review_type, Integer ai_score, Instant completed_at) {
}
