DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    id    BIGINT(20)  NOT NULL COMMENT '主键ID',
    name  VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    age   INT(11)     NULL DEFAULT NULL COMMENT '年龄',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (id)
);

CREATE TABLE user2
(
    id   BIGINT(20)  NOT NULL COMMENT '主键ID',
    name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    age  INT(11)     NULL DEFAULT NULL COMMENT '年龄',
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS app;

CREATE TABLE app(
    id                    BIGINT(20)  AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    app_name              VARCHAR(30) NULL DEFAULT NULL COMMENT '名称',
    os_switch             INT(4)      NULL DEFAULT NULL COMMENT '系统开关',
    os_version            VARCHAR(50) NULL DEFAULT NULL COMMENT '系统版本',
    app_switch            INT(4)      NULL DEFAULT NULL COMMENT '应用开关',
    app_version           VARCHAR(50) NULL DEFAULT NULL COMMENT '应用版本',
    push_protocol_version VARCHAR(50) NULL DEFAULT NULL COMMENT 'push协议版本',
    subscribe_switch      INT(4)      NULL DEFAULT NULL COMMENT '订阅开关',
    frequency_id          INT(20)     NULL DEFAULT NULL COMMENT '频控id',
    keyword_id            INT(20)     NULL DEFAULT NULL COMMENT '关键字id',
    black_list_id         INT(20)     NULL DEFAULT NULL COMMENT '黑名单id',
    white_list_id         INT(20)     NULL DEFAULT NULL COMMENT '白名单id',
    erp                   VARCHAR(50) NULL DEFAULT NULL COMMENT 'erp账号',
    create_time           DATETIME    NULL DEFAULT CURRENT_TIMESTAMP COMMENT  '创建时间',
    update_time           DATETIME    NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '更新时间',
    yn                    INT(2)      NULL DEFAULT 1 COMMENT '有效状态,1有效 0无效',
    PRIMARY KEY (id)
);