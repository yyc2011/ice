package com.ice.domain;

public enum TopicStatus {
    REVIEWING((byte) 0, "reviewing"),
    ONGOING((byte) 1, "ongoing"),
    ENDED((byte) 2, "ended"),
    REJECTED((byte) 3, "rejected"),
    OFFLINE((byte) 4, "offline");

    private final byte code;
    private final String apiValue;

    TopicStatus(int code, String apiValue) {
        this.code = (byte) code;
        this.apiValue = apiValue;
    }

    public byte code() {
        return code;
    }

    public String apiValue() {
        return apiValue;
    }

    public static TopicStatus fromCode(byte code) {
        for (TopicStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown topic status: " + code);
    }

    public static TopicStatus fromApiValue(String apiValue) {
        if (apiValue == null) {
            throw new IllegalArgumentException("status is required");
        }
        for (TopicStatus status : values()) {
            if (status.apiValue.equals(apiValue)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown topic status: " + apiValue);
    }
}
