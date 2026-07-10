CREATE TABLE notification (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    user_id    BIGINT       NOT NULL,
    type       VARCHAR(32)  NOT NULL,
    title      VARCHAR(128) NOT NULL,
    content    TEXT         NOT NULL,
    ref_type   VARCHAR(32)           DEFAULT NULL,
    ref_id     BIGINT                DEFAULT NULL,
    is_read    TINYINT      NOT NULL DEFAULT 0,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_notification_user (user_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
