SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for stock
-- ----------------------------
DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT 'name',
  `count` int(11) NOT NULL COMMENT 'stock',
  `sale` int(11) NOT NULL COMMENT 'sold',
  `version` int(11) NOT NULL COMMENT 'optimistic locking',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of stock
-- ----------------------------
BEGIN;
INSERT INTO `stock` VALUES (1, 'iphone6', 1000, 62, 503);
COMMIT;

-- ----------------------------
-- Table structure for stock_order
-- ----------------------------
DROP TABLE IF EXISTS `stock_order`;
CREATE TABLE `stock_order` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sid` int(11) NOT NULL COMMENT 'staockID',
  `name` varchar(30) NOT NULL DEFAULT '' COMMENT 'name of item',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3216 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `name` varchar(80) DEFAULT NULL COMMENT 'user name',
  `password` varchar(40) DEFAULT NULL COMMENT 'password',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, 'xiaochen', '123456');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
