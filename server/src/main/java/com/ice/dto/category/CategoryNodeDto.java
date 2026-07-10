package com.ice.dto.category;

import java.util.List;

public record CategoryNodeDto(
        long id,
        String name,
        List<CategoryNodeDto> children
) {
}
