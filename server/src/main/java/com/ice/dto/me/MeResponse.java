package com.ice.dto.me;

public record MeResponse(
        long id,
        String nickname,
        String avatar_url,
        String level,
        String level_name,
        int book_coin_balance,
        int follower_count,
        int following_count,
        int article_count,
        String role,
        int word_limit
) {
}
