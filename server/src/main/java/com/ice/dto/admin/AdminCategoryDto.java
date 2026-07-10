package com.ice.dto.admin;

public record AdminCategoryDto(
        long id,
        long parent_id,
        String name,
        boolean is_home_recommended,
        int home_sort_order
) {
}
