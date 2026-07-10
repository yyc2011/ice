package com.ice.dto.auth;

public record SmsCodeRequest(
        String phone,
        String purpose
) {
}
