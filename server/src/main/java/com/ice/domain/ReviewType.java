package com.ice.domain;

public enum ReviewType {
    INITIAL_AI(1, "initial"),
    AI_APPEAL(2, "ai_appeal"),
    MANUAL(3, "manual");

    private final byte code;
    private final String apiValue;

    ReviewType(int code, String apiValue) {
        this.code = (byte) code;
        this.apiValue = apiValue;
    }

    public byte code() {
        return code;
    }

    public String apiValue() {
        return apiValue;
    }
}
