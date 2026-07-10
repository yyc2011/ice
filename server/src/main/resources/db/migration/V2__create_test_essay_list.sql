CREATE TABLE test_essay_list (
    id    INT           NOT NULL AUTO_INCREMENT,
    title VARCHAR(1024) NOT NULL DEFAULT '',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO test_essay_list (title) VALUES
    ('第一篇测试文章'),
    ('第二篇测试文章'),
    ('第三篇测试文章');
