DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) not null auto_increment,
  `name` varchar(255) default null,
  `age` int(11) default null,
  `sex` varchar(255) default null,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET =utf8mb4;