package com.ice.dto.category;

import java.util.List;

public record CategoryPreviewArticleDto(
        long id,
        String title,
        String cover_url,
        double hot_score
) {
}
