package com.ice.domain;

public enum ContentType {
    ARTICLE(1),
    TOPIC(2);

    private final byte code;

    ContentType(int code) {
        this.code = (byte) code;
    }

    public byte code() {
        return code;
    }
}
