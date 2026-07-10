package com.ice.dto.admin;

public record AnnouncementDto(
        long id,
        String title,
        String content,
        boolean published,
        String published_at,
        String created_at
) {
}
