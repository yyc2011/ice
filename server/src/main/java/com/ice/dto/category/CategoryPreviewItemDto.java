package com.ice.dto.category;

import java.util.List;

public record CategoryPreviewItemDto(
        long id,
        String name,
        List<CategoryPreviewArticleDto> articles
) {
}
