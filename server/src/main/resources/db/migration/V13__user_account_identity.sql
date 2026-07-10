-- internal_uid：系统内部 15 位数字标识；account_name：用户自定义登录名（可空）
ALTER TABLE `user`
    ADD COLUMN internal_uid CHAR(15) NULL COMMENT '内部唯一标识，15位数字，不可改' AFTER id,
    ADD COLUMN account_name VARCHAR(15) NULL COMMENT 'C端用户自定义登录名' AFTER login_name,
    ADD COLUMN account_name_changed_at DATETIME NULL COMMENT '账号上次变更时间' AFTER account_name;

-- 存量用户回填 internal_uid（基于 id 生成唯一 15 位数字，前缀 1 + id 左补零）
UPDATE `user`
SET internal_uid = CONCAT('1', LPAD(id, 14, '0'))
WHERE internal_uid IS NULL;

ALTER TABLE `user`
    MODIFY internal_uid CHAR(15) NOT NULL,
    ADD UNIQUE KEY uk_user_internal_uid (internal_uid),
    ADD UNIQUE KEY uk_user_account_name (account_name);
