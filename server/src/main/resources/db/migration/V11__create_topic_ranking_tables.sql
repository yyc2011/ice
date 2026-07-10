CREATE TABLE topic (
    id                   BIGINT       NOT NULL AUTO_INCREMENT,
    user_id              BIGINT       NOT NULL,
    title                VARCHAR(20)  NOT NULL,
    description          VARCHAR(200)          DEFAULT NULL,
    cover_url            VARCHAR(512)          DEFAULT NULL,
    duration_days        INT          NOT NULL DEFAULT 14,
    reward_pool_amount   INT          NOT NULL DEFAULT 0,
    reward_top_n         TINYINT      NOT NULL DEFAULT 3,
    reward_ratio         VARCHAR(16)           DEFAULT '5:3:2',
    status               TINYINT      NOT NULL DEFAULT 0 COMMENT '0审核中 1进行中 2已结束 3已拒绝 4已下架',
    reject_reason        VARCHAR(512)          DEFAULT NULL,
    start_at             DATETIME              DEFAULT NULL,
    end_at               DATETIME              DEFAULT NULL,
    article_count        INT          NOT NULL DEFAULT 0,
    view_count           INT          NOT NULL DEFAULT 0,
    created_at           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_topic_status (status),
    KEY idx_topic_start (start_at),
    KEY idx_topic_end (end_at),
    KEY idx_topic_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE ranking_config (
    config_key   VARCHAR(64)  NOT NULL,
    config_value VARCHAR(512) NOT NULL,
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO ranking_config (config_key, config_value) VALUES
    ('hot_weight_view_ln', '10'),
    ('hot_weight_like', '3'),
    ('hot_weight_comment', '10'),
    ('hot_weight_dislike', '2'),
    ('hot_min_likes', '1'),
    ('hot_min_views', '20'),
    ('featured_days', '7'),
    ('featured_ai_weight', '0.7'),
    ('featured_fresh_weight', '0.3'),
    ('category_hot_days', '30');

CREATE TABLE hotrank_daily (
    id           BIGINT        NOT NULL AUTO_INCREMENT,
    rank_date    DATE          NOT NULL,
    article_id   BIGINT        NOT NULL,
    `rank`       TINYINT       NOT NULL,
    hot_score    DECIMAL(12,4) NOT NULL DEFAULT 0,
    reward_coin  INT           NOT NULL DEFAULT 0,
    created_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_hotrank_date_article (rank_date, article_id),
    KEY idx_hotrank_date_rank (rank_date, `rank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 演示种子话题（进行中 3 个 + 即将开始 1 个 + 已结束 2 个）
INSERT INTO topic (user_id, title, description, cover_url, duration_days, status, start_at, end_at, article_count, view_count, created_at, updated_at)
SELECT u.id, '以回家为题', '每一次离开，都是另一种回家……', NULL, 14, 1,
       DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_ADD(NOW(), INTERVAL 11 DAY), 234, 42000,
       NOW(), NOW()
FROM user u WHERE u.openid = 'dev-test' LIMIT 1;

INSERT INTO topic (user_id, title, description, cover_url, duration_days, status, start_at, end_at, article_count, view_count, created_at, updated_at)
SELECT u.id, '春天写', '记录春天的点滴与感动', NULL, 7, 1,
       DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 6 DAY), 28, 8500,
       NOW(), NOW()
FROM user u WHERE u.openid = 'dev-test' LIMIT 1;

INSERT INTO topic (user_id, title, description, cover_url, duration_days, status, start_at, end_at, article_count, view_count, created_at, updated_at)
SELECT u.id, '乡愁', '写给故乡的一封信', NULL, 14, 1,
       DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_ADD(NOW(), INTERVAL 9 DAY), 9, 3200,
       NOW(), NOW()
FROM user u WHERE u.openid = 'dev-test' LIMIT 1;

INSERT INTO topic (user_id, title, description, duration_days, status, start_at, end_at, article_count, created_at, updated_at)
SELECT u.id, '夏天主题征文', '用文字记录这个夏天', 14, 0,
       DATE_ADD(NOW(), INTERVAL 3 DAY), DATE_ADD(NOW(), INTERVAL 17 DAY), 0,
       NOW(), NOW()
FROM user u WHERE u.openid = 'dev-test' LIMIT 1;

INSERT INTO topic (user_id, title, description, duration_days, status, start_at, end_at, article_count, view_count, created_at, updated_at)
SELECT u.id, '以归途为题', '归途上的风景与心情', 14, 2,
       DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 16 DAY), 589, 42000,
       NOW(), NOW()
FROM user u WHERE u.openid = 'dev-test' LIMIT 1;

INSERT INTO topic (user_id, title, description, duration_days, status, start_at, end_at, article_count, view_count, created_at, updated_at)
SELECT u.id, '写给二十岁的自己', '如果回到二十岁', 7, 2,
       DATE_SUB(NOW(), INTERVAL 60 DAY), DATE_SUB(NOW(), INTERVAL 53 DAY), 412, 31000,
       NOW(), NOW()
FROM user u WHERE u.openid = 'dev-test' LIMIT 1;
