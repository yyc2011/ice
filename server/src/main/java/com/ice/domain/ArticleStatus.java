package com.ice.domain;

public enum ArticleStatus {
    DRAFT(0),
    REVIEWING(1),
    PUBLISHED(2),
    REJECTED(3),
    DELETED(4);

    private final byte code;

    ArticleStatus(int code) {
        this.code = (byte) code;
    }

    public byte code() {
        return code;
    }

    public static ArticleStatus fromCode(byte code) {
        for (ArticleStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown article status: " + code);
    }

    public String apiValue() {
        return name().toLowerCase();
    }

    public static ArticleStatus fromApiValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("status is required");
        }
        for (ArticleStatus status : values()) {
            if (status.apiValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown article status: " + value);
    }
}
