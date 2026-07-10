package com.ice.dto.auth;

public record UserSummary(
        long id,
        String nickname,
        String avatar_url,
        String level,
        String level_name,
        int book_coin_balance,
        String role
) {
}
