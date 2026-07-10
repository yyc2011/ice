package com.ice.domain;

public enum ReviewStatus {
    PENDING(0),
    PASSED(1),
    REJECTED(2);

    private final byte code;

    ReviewStatus(int code) {
        this.code = (byte) code;
    }

    public byte code() {
        return code;
    }
}
