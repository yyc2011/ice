CREATE TABLE book_coin_config (
    config_key   VARCHAR(64)  NOT NULL,
    config_value VARCHAR(256) NOT NULL,
    remark       VARCHAR(256)          DEFAULT NULL,
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE book_coin_log (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    user_id       BIGINT       NOT NULL,
    amount        INT          NOT NULL,
    balance_after INT          NOT NULL,
    type          VARCHAR(64)  NOT NULL,
    ref_type      VARCHAR(32)           DEFAULT NULL,
    ref_id        BIGINT                DEFAULT NULL,
    remark        VARCHAR(256)          DEFAULT NULL,
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_book_coin_log_user (user_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO book_coin_config (config_key, config_value, remark) VALUES
    ('reward_publish_article', '5', '发文审核通过奖励书币'),
    ('reward_publish_daily_limit', '3', '每日发文奖励篇数上限'),
    ('cost_review_ai', '10', 'AI复审扣费'),
    ('cost_review_manual', '30', '人工复审扣费');
