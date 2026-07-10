CREATE TABLE announcement (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    title        VARCHAR(100) NOT NULL,
    content      TEXT         NOT NULL,
    publisher_id BIGINT       NOT NULL,
    status       TINYINT      NOT NULL DEFAULT 0 COMMENT '0草稿 1已发布',
    published_at DATETIME              DEFAULT NULL,
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
