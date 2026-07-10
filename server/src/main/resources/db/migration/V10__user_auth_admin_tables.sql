-- user 认证扩展 + role 默认值修订（0超管 1管理员 2普通用户；值越小权限越高）
ALTER TABLE `user`
    MODIFY COLUMN openid VARCHAR(64) NULL COMMENT '微信 openid；纯运营账号可为 NULL',
    MODIFY COLUMN role TINYINT NOT NULL DEFAULT 2 COMMENT '0超管 1管理员 2普通用户',
    ADD COLUMN login_name VARCHAR(64) NULL COMMENT '后台登录名' AFTER openid,
    ADD COLUMN password_hash VARCHAR(255) NULL COMMENT 'bcrypt' AFTER login_name,
    ADD COLUMN phone VARCHAR(20) NULL COMMENT '手机号' AFTER password_hash,
    ADD COLUMN email VARCHAR(128) NULL COMMENT '邮箱' AFTER phone,
    ADD COLUMN phone_verified TINYINT NOT NULL DEFAULT 0 AFTER email,
    ADD COLUMN email_verified TINYINT NOT NULL DEFAULT 0 AFTER phone_verified;

ALTER TABLE `user`
    ADD UNIQUE KEY uk_user_login_name (login_name),
    ADD UNIQUE KEY uk_user_phone (phone),
    ADD UNIQUE KEY uk_user_email (email);

UPDATE `user` SET role = 2 WHERE openid = 'dev-test';

CREATE TABLE report (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    reporter_id    BIGINT       NOT NULL,
    target_type    TINYINT      NOT NULL COMMENT '1文章 2评论',
    target_id      BIGINT       NOT NULL,
    reason         VARCHAR(32)  NOT NULL,
    reason_detail  VARCHAR(256)          DEFAULT NULL,
    status         TINYINT      NOT NULL DEFAULT 0 COMMENT '0待处理 1成立 2不成立',
    created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_report_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE feature_config (
    config_key   VARCHAR(64)  NOT NULL,
    config_value VARCHAR(512) NOT NULL,
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO feature_config (config_key, config_value) VALUES
    ('recharge_enabled', 'false'),
    ('recharge_custom_amount_enabled', 'false');
