package com.ice.dto.article;

import java.util.List;

public record FeaturedArticlesResponse(
        List<FeaturedArticleItemDto> items,
        int total,
        int page,
        int size
) {
}
