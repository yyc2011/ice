package com.ice.dto.admin;

public record ReportDto(
        long id,
        long reporter_id,
        int target_type,
        long target_id,
        String reason,
        String reason_detail,
        int status,
        String created_at
) {
}
