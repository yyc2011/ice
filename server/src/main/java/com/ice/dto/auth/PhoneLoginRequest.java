package com.ice.dto.auth;

public record PhoneLoginRequest(
        String phone,
        String code
) {
}
