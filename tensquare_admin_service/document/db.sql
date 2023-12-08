CREATE DATABASE IF NOT EXISTS `tensquare_user` CHARACTER SET 'utf8';

USE `tensquare_user`;

DROP TABLE IF EXISTS `tb_admin`;

CREATE TABLE `tb_admin`
(
    `id`        varchar(20) NOT NULL COMMENT 'ID',
    `loginname` varchar(100) DEFAULT NULL COMMENT '登陆名称',
    `password`  varchar(100) DEFAULT NULL COMMENT '密码',
    `state`     varchar(1)   DEFAULT NULL COMMENT '状态',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员';

-- admin / 123456
insert into `tb_admin`(`id`, `loginname`, `password`, `state`)
values ('1194281533230419968', 'admin', '$2a$10$PhnLmwA7QarH8M/Wc52Mfekoyqw74Vp619M2p2dnxq0Ltz6xXTWJu', '1');
