package com.ice.dto.auth;

public record DevLoginResponse(String token, long expires_in, UserSummary user) {
}
