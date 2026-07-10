CREATE TABLE tag (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    name       VARCHAR(32)  NOT NULL,
    use_count  INT          NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_tag_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE category (
    id                  BIGINT       NOT NULL AUTO_INCREMENT,
    parent_id           BIGINT       NOT NULL DEFAULT 0 COMMENT '0=大类',
    name                VARCHAR(32)  NOT NULL,
    sort_order          INT          NOT NULL DEFAULT 0,
    is_default          TINYINT      NOT NULL DEFAULT 0,
    is_home_recommended TINYINT      NOT NULL DEFAULT 0,
    home_sort_order     INT          NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_category_parent (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 大类
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order) VALUES
    (0, '综合',     1, 0, 1, 1),
    (0, '生活情感', 2, 0, 1, 2),
    (0, '城市旅行', 3, 0, 1, 3),
    (0, '创作技艺', 4, 0, 1, 4),
    (0, '自然观察', 5, 0, 1, 5),
    (0, '思考随笔', 6, 0, 0, 0);

-- 小类（综合）
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '生活随笔', 1, 1, 0, 0 FROM category WHERE parent_id = 0 AND name = '综合';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '日常随想', 2, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '综合';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '城市漫步', 3, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '综合';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '人间观察', 4, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '综合';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '片刻记录', 5, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '综合';

-- 小类（生活情感）
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '亲情', 1, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '生活情感';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '爱情', 2, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '生活情感';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '友情', 3, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '生活情感';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '成长', 4, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '生活情感';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '治愈', 5, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '生活情感';

-- 小类（城市旅行）
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '旅途见闻', 1, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '城市旅行';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '街巷故事', 2, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '城市旅行';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '异乡生活', 3, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '城市旅行';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '美食探店', 4, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '城市旅行';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '人文地标', 5, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '城市旅行';

-- 小类（创作技艺）
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '诗歌', 1, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '创作技艺';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '散文', 2, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '创作技艺';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '小说片段', 3, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '创作技艺';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '日记体', 4, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '创作技艺';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '写作心得', 5, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '创作技艺';

-- 小类（自然观察）
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '四季风物', 1, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '自然观察';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '动植物', 2, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '自然观察';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '天气与心情', 3, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '自然观察';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '乡间田野', 4, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '自然观察';

-- 小类（思考随笔）
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '社会观察', 1, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '思考随笔';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '读书笔记', 2, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '思考随笔';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '职场', 3, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '思考随笔';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '教育', 4, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '思考随笔';
INSERT INTO category (parent_id, name, sort_order, is_default, is_home_recommended, home_sort_order)
SELECT id, '哲学漫谈', 5, 0, 0, 0 FROM category WHERE parent_id = 0 AND name = '思考随笔';

-- 常用标签种子
INSERT INTO tag (name) VALUES ('生活'), ('随笔'), ('情感');
