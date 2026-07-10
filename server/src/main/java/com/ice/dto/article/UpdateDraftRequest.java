package com.ice.dto.article;

public record UpdateDraftRequest(
        String title,
        String content,
        java.util.List<Long> tag_ids,
        Long category_id,
        Long topic_id,
        String cover_url,
        java.util.List<String> image_urls
) {
}
