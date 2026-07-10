package com.ice.dto.category;

import java.util.List;

public record CategoryTreeNodeDto(
        long id,
        String name,
        int sort_order,
        boolean is_default,
        List<CategoryTreeNodeDto> children
) {
}
