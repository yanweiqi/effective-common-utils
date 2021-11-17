DROP TABLE IF EXISTS app;

CREATE TABLE app
(
    id                    BIGINT(20)  NOT NULL COMMENT '主键ID',
    app_name                  VARCHAR(30) NULL DEFAULT NULL COMMENT '名称',
    os_switch             VARCHAR(50) NULL DEFAULT NULL COMMENT '系统开关',
    os_version            VARCHAR(50) NULL DEFAULT NULL COMMENT '系统版本',
    app_switch            VARCHAR(50) NULL DEFAULT NULL COMMENT '应用开关',
    app_version           VARCHAR(50) NULL DEFAULT NULL COMMENT '应用版本',
    push_protocol_version VARCHAR(50) NULL DEFAULT NULL COMMENT 'push协议版本',
    subscribe_switch      VARCHAR(50) NULL DEFAULT NULL COMMENT '订阅开关',
    frequency_id          INT(20)     NULL DEFAULT NULL COMMENT '频控id',
    keyword_id            INT(20)     NULL DEFAULT NULL COMMENT '关键字id',
    black_list_id         INT(20)     NULL DEFAULT NULL COMMENT '黑名单id',
    white_list_id         INT(20)     NULL DEFAULT NULL COMMENT '白名单id',
    PRIMARY KEY (id)
);