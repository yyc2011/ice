package com.ice.dto.me;

public record BindPhoneRequest(
        String phone,
        String code
) {
}
