CREATE TABLE content_review (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    content_type   TINYINT      NOT NULL COMMENT '1文章 2话题',
    content_id     BIGINT       NOT NULL,
    review_type    TINYINT      NOT NULL COMMENT '1首次AI 2AI复审 3人工复审',
    status         TINYINT      NOT NULL DEFAULT 0 COMMENT '0待处理 1通过 2拒绝',
    appeal_text    TEXT                  DEFAULT NULL,
    reject_reason  VARCHAR(512)          DEFAULT NULL,
    reviewer_id    BIGINT                DEFAULT NULL,
    ai_score       SMALLINT              DEFAULT NULL,
    ai_dimensions  JSON                  DEFAULT NULL,
    created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at   DATETIME              DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_review_queue (content_type, status, created_at),
    KEY idx_review_content (content_id, content_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
