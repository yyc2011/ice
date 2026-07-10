package com.ice.dto.admin;

public record AdminCategoryRequest(
        Long parent_id,
        String name,
        Integer sort_order,
        Boolean is_home_recommended,
        Integer home_sort_order
) {
}
