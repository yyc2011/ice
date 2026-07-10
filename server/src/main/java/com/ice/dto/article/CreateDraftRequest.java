package com.ice.dto.article;

import java.util.List;

public record CreateDraftRequest(
        String title,
        String content,
        List<Long> tag_ids,
        Long category_id,
        Long topic_id,
        String cover_url,
        List<String> image_urls
) {
}
