package com.ice.dto.admin;

import java.util.List;

public record ReviewListResponse(
        List<ReviewListItemDto> items,
        long total,
        int page,
        int size
) {
}
