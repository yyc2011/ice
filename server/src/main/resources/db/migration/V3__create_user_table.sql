CREATE TABLE `user` (
    id                      BIGINT       NOT NULL AUTO_INCREMENT,
    openid                  VARCHAR(64)  NOT NULL,
    nickname                VARCHAR(15)  NOT NULL DEFAULT '微信用户',
    avatar_url              VARCHAR(512) NOT NULL DEFAULT '',
    bio                     VARCHAR(1024)         DEFAULT NULL,
    level                   SMALLINT     NOT NULL DEFAULT 0,
    level_score             INT          NOT NULL DEFAULT 0,
    book_coin_balance       INT          NOT NULL DEFAULT 0,
    article_count           INT          NOT NULL DEFAULT 0,
    total_likes_received    INT          NOT NULL DEFAULT 0,
    hotrank_top10_count     INT          NOT NULL DEFAULT 0,
    follower_count          INT          NOT NULL DEFAULT 0,
    following_count         INT          NOT NULL DEFAULT 0,
    role                    TINYINT      NOT NULL DEFAULT 0 COMMENT '0普通 1管理员 2超管',
    status                  TINYINT      NOT NULL DEFAULT 0 COMMENT '0正常 1封禁',
    created_at              DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_openid (openid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 本地联调默认用户（openid=dev-test）
INSERT INTO `user` (openid, nickname, book_coin_balance) VALUES
    ('dev-test', '联调测试用户', 100);
