package com.ice.dto.search;

public record SearchUserItemDto(
        long id,
        String nickname,
        String avatar_url,
        String bio,
        int article_count,
        int like_count
) {
}
