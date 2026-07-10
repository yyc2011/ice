-- 修复历史迁移写入时的中文乱码，并补充首页演示文章种子数据

-- ── 1. 修正用户 / 标签 / 分类中文 ──────────────────────────────
UPDATE `user` SET nickname = '联调测试用户' WHERE openid = 'dev-test';

UPDATE tag SET name = '生活' WHERE id = 1;
UPDATE tag SET name = '随笔' WHERE id = 2;
UPDATE tag SET name = '情感' WHERE id = 3;

UPDATE category SET name = '综合'     WHERE parent_id = 0 AND sort_order = 1;
UPDATE category SET name = '生活情感' WHERE parent_id = 0 AND sort_order = 2;
UPDATE category SET name = '城市旅行' WHERE parent_id = 0 AND sort_order = 3;
UPDATE category SET name = '创作技艺' WHERE parent_id = 0 AND sort_order = 4;
UPDATE category SET name = '自然观察' WHERE parent_id = 0 AND sort_order = 5;
UPDATE category SET name = '思考随笔' WHERE parent_id = 0 AND sort_order = 6;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '生活随笔'
WHERE p.name = '综合' AND c.sort_order = 1;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '日常随想'
WHERE p.name = '综合' AND c.sort_order = 2;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '城市漫步'
WHERE p.name = '综合' AND c.sort_order = 3;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '人间观察'
WHERE p.name = '综合' AND c.sort_order = 4;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '片刻记录'
WHERE p.name = '综合' AND c.sort_order = 5;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '亲情'
WHERE p.name = '生活情感' AND c.sort_order = 1;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '爱情'
WHERE p.name = '生活情感' AND c.sort_order = 2;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '友情'
WHERE p.name = '生活情感' AND c.sort_order = 3;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '成长'
WHERE p.name = '生活情感' AND c.sort_order = 4;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '治愈'
WHERE p.name = '生活情感' AND c.sort_order = 5;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '旅途见闻'
WHERE p.name = '城市旅行' AND c.sort_order = 1;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '街巷故事'
WHERE p.name = '城市旅行' AND c.sort_order = 2;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '异乡生活'
WHERE p.name = '城市旅行' AND c.sort_order = 3;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '美食探店'
WHERE p.name = '城市旅行' AND c.sort_order = 4;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '人文地标'
WHERE p.name = '城市旅行' AND c.sort_order = 5;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '诗歌'
WHERE p.name = '创作技艺' AND c.sort_order = 1;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '散文'
WHERE p.name = '创作技艺' AND c.sort_order = 2;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '小说片段'
WHERE p.name = '创作技艺' AND c.sort_order = 3;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '日记体'
WHERE p.name = '创作技艺' AND c.sort_order = 4;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '写作心得'
WHERE p.name = '创作技艺' AND c.sort_order = 5;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '四季风物'
WHERE p.name = '自然观察' AND c.sort_order = 1;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '动植物'
WHERE p.name = '自然观察' AND c.sort_order = 2;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '天气与心情'
WHERE p.name = '自然观察' AND c.sort_order = 3;

UPDATE category c
JOIN category p ON c.parent_id = p.id
SET c.name = '乡间田野'
WHERE p.name = '自然观察' AND c.sort_order = 4;

-- ── 2. 修正话题中文 ─────────────────────────────────────────────
UPDATE topic SET title = '以回家为题', description = '每一次离开，都是另一种回家……'
WHERE status = 1 AND article_count = 234;

UPDATE topic SET title = '春天写', description = '记录春天的点滴与感动'
WHERE status = 1 AND article_count = 28;

UPDATE topic SET title = '乡愁', description = '写给故乡的一封信'
WHERE status = 1 AND article_count = 9;

UPDATE topic SET title = '夏天主题征文', description = '用文字记录这个夏天'
WHERE status = 0 AND article_count = 0 AND duration_days = 14
  AND start_at > NOW();

UPDATE topic SET title = '以归途为题', description = '归途上的风景与心情'
WHERE status = 2 AND article_count = 589;

UPDATE topic SET title = '写给二十岁的自己', description = '如果回到二十岁'
WHERE status = 2 AND article_count = 412;

-- ── 3. 演示作者（首页热榜展示不同昵称）──────────────────────────
INSERT INTO `user` (internal_uid, openid, nickname, book_coin_balance)
SELECT CONCAT('1', LPAD(COALESCE(MAX(id), 0) + 1, 14, '0')), 'demo-author-1', '张三', 50 FROM `user`
ON DUPLICATE KEY UPDATE nickname = VALUES(nickname);

INSERT INTO `user` (internal_uid, openid, nickname, book_coin_balance)
SELECT CONCAT('1', LPAD(COALESCE(MAX(id), 0) + 1, 14, '0')), 'demo-author-2', '李四', 50 FROM `user`
ON DUPLICATE KEY UPDATE nickname = VALUES(nickname);

INSERT INTO `user` (internal_uid, openid, nickname, book_coin_balance)
SELECT CONCAT('1', LPAD(COALESCE(MAX(id), 0) + 1, 14, '0')), 'demo-author-3', '王五', 50 FROM `user`
ON DUPLICATE KEY UPDATE nickname = VALUES(nickname);

INSERT INTO `user` (internal_uid, openid, nickname, book_coin_balance)
SELECT CONCAT('1', LPAD(COALESCE(MAX(id), 0) + 1, 14, '0')), 'demo-author-4', '赵六', 50 FROM `user`
ON DUPLICATE KEY UPDATE nickname = VALUES(nickname);

INSERT INTO `user` (internal_uid, openid, nickname, book_coin_balance)
SELECT CONCAT('1', LPAD(COALESCE(MAX(id), 0) + 1, 14, '0')), 'demo-author-5', '陈七', 50 FROM `user`
ON DUPLICATE KEY UPDATE nickname = VALUES(nickname);

INSERT INTO `user` (internal_uid, openid, nickname, book_coin_balance)
SELECT CONCAT('1', LPAD(COALESCE(MAX(id), 0) + 1, 14, '0')), 'demo-author-6', '孙八', 50 FROM `user`
ON DUPLICATE KEY UPDATE nickname = VALUES(nickname);

-- ── 4. 演示文章（status=2 已发布，满足热榜/精选/分类预览条件）──
INSERT INTO article (
    user_id, title, content, category_id, topic_id, word_count, status,
    like_count, comment_count, view_count, ai_quality_score, published_at, created_at, updated_at
)
SELECT u.id, '这一夜我把灯关掉',
       '深夜的城市只剩下路灯，我把所有灯关掉，坐在黑暗里听自己的心跳。',
       (SELECT c.id FROM category c JOIN category p ON c.parent_id = p.id WHERE p.name = '综合' AND c.name = '生活随笔' LIMIT 1),
       (SELECT t.id FROM topic t WHERE t.title = '以回家为题' AND t.status = 1 LIMIT 1),
       680, 2, 342, 28, 1200, 88, DATE_SUB(NOW(), INTERVAL 2 HOUR), NOW(), NOW()
FROM `user` u WHERE u.openid = 'demo-author-1' AND NOT EXISTS (SELECT 1 FROM article a WHERE a.title = '这一夜我把灯关掉') LIMIT 1;

INSERT INTO article (
    user_id, title, content, category_id, topic_id, word_count, status,
    like_count, comment_count, view_count, ai_quality_score, published_at, created_at, updated_at
)
SELECT u.id, '外婆的红薯地',
       '秋天回外婆家，红薯地里还留着去年的藤蔓，泥土温热，像她的手。',
       (SELECT c.id FROM category c JOIN category p ON c.parent_id = p.id WHERE p.name = '生活情感' AND c.name = '亲情' LIMIT 1),
       (SELECT t.id FROM topic t WHERE t.title = '乡愁' AND t.status = 1 LIMIT 1),
       520, 2, 289, 19, 986, 86, DATE_SUB(NOW(), INTERVAL 5 HOUR), NOW(), NOW()
FROM `user` u WHERE u.openid = 'demo-author-2' AND NOT EXISTS (SELECT 1 FROM article a WHERE a.title = '外婆的红薯地') LIMIT 1;

INSERT INTO article (
    user_id, title, content, category_id, word_count, status,
    like_count, comment_count, view_count, ai_quality_score, published_at, created_at, updated_at
)
SELECT u.id, '写给二十岁的自己',
       '如果回到二十岁，我想告诉那个少年，慢一点也没关系。',
       (SELECT c.id FROM category c JOIN category p ON c.parent_id = p.id WHERE p.name = '生活情感' AND c.name = '成长' LIMIT 1),
       450, 2, 201, 15, 876, 84, DATE_SUB(NOW(), INTERVAL 8 HOUR), NOW(), NOW()
FROM `user` u WHERE u.openid = 'demo-author-3' AND NOT EXISTS (SELECT 1 FROM article a WHERE a.title = '写给二十岁的自己') LIMIT 1;

INSERT INTO article (
    user_id, title, content, category_id, word_count, status,
    like_count, comment_count, view_count, ai_quality_score, published_at, created_at, updated_at
)
SELECT u.id, '凌晨三点的工地',
       '塔吊的灯还亮着，工人们说再干一会儿就能回家看孩子醒来。',
       (SELECT c.id FROM category c JOIN category p ON c.parent_id = p.id WHERE p.name = '城市旅行' AND c.name = '街巷故事' LIMIT 1),
       390, 2, 178, 12, 765, 82, DATE_SUB(NOW(), INTERVAL 12 HOUR), NOW(), NOW()
FROM `user` u WHERE u.openid = 'demo-author-4' AND NOT EXISTS (SELECT 1 FROM article a WHERE a.title = '凌晨三点的工地') LIMIT 1;

INSERT INTO article (
    user_id, title, content, category_id, topic_id, word_count, status,
    like_count, comment_count, view_count, ai_quality_score, published_at, created_at, updated_at
)
SELECT u.id, '写给故乡的信',
       '故乡的信没有地址，只有风经过稻田时留下的气味。',
       (SELECT c.id FROM category c JOIN category p ON c.parent_id = p.id WHERE p.name = '创作技艺' AND c.name = '散文' LIMIT 1),
       (SELECT t.id FROM topic t WHERE t.title = '乡愁' AND t.status = 1 LIMIT 1),
       410, 2, 134, 9, 643, 80, DATE_SUB(NOW(), INTERVAL 18 HOUR), NOW(), NOW()
FROM `user` u WHERE u.openid = 'demo-author-5' AND NOT EXISTS (SELECT 1 FROM article a WHERE a.title = '写给故乡的信') LIMIT 1;

INSERT INTO article (
    user_id, title, content, category_id, word_count, status,
    like_count, comment_count, view_count, ai_quality_score, published_at, created_at, updated_at
)
SELECT u.id, '菜市场的老人',
       '在菜市场遇见的老人，用旧报纸包着刚买的青菜，像包着一段往事。',
       (SELECT c.id FROM category c JOIN category p ON c.parent_id = p.id WHERE p.name = '综合' AND c.name = '人间观察' LIMIT 1),
       360, 2, 98, 7, 520, 78, DATE_SUB(NOW(), INTERVAL 20 HOUR), NOW(), NOW()
FROM `user` u WHERE u.openid = 'demo-author-6' AND NOT EXISTS (SELECT 1 FROM article a WHERE a.title = '菜市场的老人') LIMIT 1;

INSERT INTO article (
    user_id, title, content, category_id, word_count, status,
    like_count, comment_count, view_count, ai_quality_score, published_at, created_at, updated_at
)
SELECT u.id, '我的第一份工资',
       '第一份工资不多，却足够请爸妈吃一顿像样的饭。',
       (SELECT c.id FROM category c JOIN category p ON c.parent_id = p.id WHERE p.name = '生活情感' AND c.name = '治愈' LIMIT 1),
       340, 2, 76, 6, 410, 83, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW(), NOW()
FROM `user` u WHERE u.openid = 'demo-author-1' AND NOT EXISTS (SELECT 1 FROM article a WHERE a.title = '我的第一份工资') LIMIT 1;

INSERT INTO article (
    user_id, title, content, category_id, word_count, status,
    like_count, comment_count, view_count, ai_quality_score, published_at, created_at, updated_at
)
SELECT u.id, '深夜高速收费站',
       '深夜的高速收费站，卡车司机递来一杯热水，暖了整个冬天。',
       (SELECT c.id FROM category c JOIN category p ON c.parent_id = p.id WHERE p.name = '城市旅行' AND c.name = '旅途见闻' LIMIT 1),
       370, 2, 65, 5, 380, 81, DATE_SUB(NOW(), INTERVAL 4 DAY), NOW(), NOW()
FROM `user` u WHERE u.openid = 'demo-author-2' AND NOT EXISTS (SELECT 1 FROM article a WHERE a.title = '深夜高速收费站') LIMIT 1;

INSERT INTO article (
    user_id, title, content, category_id, word_count, status,
    like_count, comment_count, view_count, ai_quality_score, published_at, created_at, updated_at
)
SELECT u.id, '我妈做的那碗面',
       '我妈做的那碗面，汤总是偏咸，却是我离家后最想念的味道。',
       (SELECT c.id FROM category c JOIN category p ON c.parent_id = p.id WHERE p.name = '生活情感' AND c.name = '亲情' LIMIT 1),
       320, 2, 58, 4, 350, 85, DATE_SUB(NOW(), INTERVAL 5 DAY), NOW(), NOW()
FROM `user` u WHERE u.openid = 'demo-author-3' AND NOT EXISTS (SELECT 1 FROM article a WHERE a.title = '我妈做的那碗面') LIMIT 1;

INSERT INTO article (
    user_id, title, content, category_id, word_count, status,
    like_count, comment_count, view_count, ai_quality_score, published_at, created_at, updated_at
)
SELECT u.id, '三月的风吹操场',
       '三月的风吹过操场，槐花落了一地，像下了一场安静的雪。',
       (SELECT c.id FROM category c JOIN category p ON c.parent_id = p.id WHERE p.name = '自然观察' AND c.name = '四季风物' LIMIT 1),
       300, 2, 45, 3, 290, 79, DATE_SUB(NOW(), INTERVAL 6 DAY), NOW(), NOW()
FROM `user` u WHERE u.openid = 'demo-author-4' AND NOT EXISTS (SELECT 1 FROM article a WHERE a.title = '三月的风吹操场') LIMIT 1;

INSERT INTO article (
    user_id, title, content, category_id, topic_id, word_count, status,
    like_count, comment_count, view_count, ai_quality_score, published_at, created_at, updated_at
)
SELECT u.id, '春天写的第一篇',
       '春天写下的第一篇文字，是关于窗台上冒芽的薄荷。',
       (SELECT c.id FROM category c JOIN category p ON c.parent_id = p.id WHERE p.name = '创作技艺' AND c.name = '日记体' LIMIT 1),
       (SELECT t.id FROM topic t WHERE t.title = '春天写' AND t.status = 1 LIMIT 1),
       280, 2, 42, 2, 260, 77, DATE_SUB(NOW(), INTERVAL 1 DAY), NOW(), NOW()
FROM `user` u WHERE u.openid = 'demo-author-5' AND NOT EXISTS (SELECT 1 FROM article a WHERE a.title = '春天写的第一篇') LIMIT 1;

INSERT INTO article (
    user_id, title, content, category_id, word_count, status,
    like_count, comment_count, view_count, ai_quality_score, published_at, created_at, updated_at
)
SELECT u.id, '落日与旷野',
       '落日把旷野染成金色，远处有牧人赶着羊群回家。',
       (SELECT c.id FROM category c JOIN category p ON c.parent_id = p.id WHERE p.name = '自然观察' AND c.name = '乡间田野' LIMIT 1),
       310, 2, 38, 2, 240, 76, DATE_SUB(NOW(), INTERVAL 2 DAY), NOW(), NOW()
FROM `user` u WHERE u.openid = 'demo-author-6' AND NOT EXISTS (SELECT 1 FROM article a WHERE a.title = '落日与旷野') LIMIT 1;

-- ── 5. 文章标签关联 ───────────────────────────────────────────────
INSERT INTO article_tag (article_id, tag_id)
SELECT a.id, t.id FROM article a, tag t
WHERE a.title = '这一夜我把灯关掉' AND t.name = '生活';

INSERT INTO article_tag (article_id, tag_id)
SELECT a.id, t.id FROM article a, tag t
WHERE a.title = '这一夜我把灯关掉' AND t.name = '随笔';

INSERT INTO article_tag (article_id, tag_id)
SELECT a.id, t.id FROM article a, tag t
WHERE a.title = '外婆的红薯地' AND t.name = '情感';

INSERT INTO article_tag (article_id, tag_id)
SELECT a.id, t.id FROM article a, tag t
WHERE a.title = '外婆的红薯地' AND t.name = '生活';

INSERT INTO article_tag (article_id, tag_id)
SELECT a.id, t.id FROM article a, tag t
WHERE a.title IN (
    '写给二十岁的自己', '凌晨三点的工地', '写给故乡的信', '菜市场的老人',
    '我的第一份工资', '深夜高速收费站', '我妈做的那碗面', '三月的风吹操场',
    '春天写的第一篇', '落日与旷野'
) AND t.name = '随笔';

-- ── 6. 同步作者发文数 ─────────────────────────────────────────────
UPDATE `user` u
SET article_count = (
    SELECT COUNT(*) FROM article a WHERE a.user_id = u.id AND a.status = 2
)
WHERE u.openid IN (
    'dev-test', 'demo-author-1', 'demo-author-2', 'demo-author-3',
    'demo-author-4', 'demo-author-5', 'demo-author-6'
);
