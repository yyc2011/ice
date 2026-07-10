package com.ice.dto.article;

import java.time.Instant;

public record CreateDraftResponse(long id, String status, int word_count, Instant created_at) {
}
