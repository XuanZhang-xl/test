-- `test`数据库 DDL

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) not null auto_increment,
  `name` varchar(255) default null,
  `age` int(11) default null,
  `sex` varchar(255) default null,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET =utf8mb4;

DROP TABLE IF EXISTS `ip_black_list`;
CREATE TABLE `ip_black_list` (
    `ip` int(11) not null auto_increment,
    PRIMARY KEY (`ip`)
) ENGINE=InnoDB DEFAULT CHARSET =utf8mb4;