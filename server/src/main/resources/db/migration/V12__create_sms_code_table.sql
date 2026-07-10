CREATE TABLE sms_code (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    phone        VARCHAR(20)  NOT NULL,
    code_hash    VARCHAR(255) NOT NULL,
    purpose      VARCHAR(32)  NOT NULL COMMENT 'login / bind',
    expire_at    DATETIME     NOT NULL,
    consumed_at  DATETIME              DEFAULT NULL,
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_sms_phone_purpose (phone, purpose, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
