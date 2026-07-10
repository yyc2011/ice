CREATE TABLE article (
    id                   BIGINT        NOT NULL AUTO_INCREMENT,
    user_id              BIGINT        NOT NULL,
    title                VARCHAR(15)   NOT NULL DEFAULT '',
    content              TEXT          NOT NULL,
    cover_url            VARCHAR(512)           DEFAULT NULL,
    category_id          BIGINT                 DEFAULT NULL,
    topic_id             BIGINT                 DEFAULT NULL,
    word_count           INT           NOT NULL DEFAULT 0,
    word_limit_purchased INT           NOT NULL DEFAULT 0,
    status               TINYINT       NOT NULL DEFAULT 0 COMMENT '0草稿 1审核中 2已发布 3已拒绝 4已删除',
    reject_reason        VARCHAR(512)           DEFAULT NULL,
    like_count           INT           NOT NULL DEFAULT 0,
    dislike_count        INT           NOT NULL DEFAULT 0,
    comment_count        INT           NOT NULL DEFAULT 0,
    view_count           INT           NOT NULL DEFAULT 0,
    ai_quality_score     SMALLINT               DEFAULT NULL,
    published_at         DATETIME               DEFAULT NULL,
    created_at           DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_article_user (user_id),
    KEY idx_article_status (status),
    KEY idx_article_published (published_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE article_image (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    article_id  BIGINT       NOT NULL,
    url         VARCHAR(512) NOT NULL,
    sort_order  TINYINT      NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    KEY idx_article_image_article (article_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE article_tag (
    article_id BIGINT NOT NULL,
    tag_id     BIGINT NOT NULL,
    PRIMARY KEY (article_id, tag_id),
    KEY idx_article_tag_tag (tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
