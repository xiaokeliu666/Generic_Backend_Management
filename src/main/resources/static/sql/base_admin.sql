/*
Navicat MySQL Data Transfer

Source Server         : Connection
Source Server Version : 50727
Source Host           : localhost:3306
Source Database       : base_admin

Target Server Type    : MYSQL
Target Server Version : 50727
File Encoding         : 65001

Date: 2020-08-20 16:46:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for persistent_logins
-- ----------------------------
DROP TABLE IF EXISTS `persistent_logins`;
CREATE TABLE `persistent_logins` (
  `series` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'id',
  `username` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'login name',
  `token` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'last time used',
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='persistent_logins table, used for ''remember-me''';

-- ----------------------------
-- Records of persistent_logins
-- ----------------------------
INSERT INTO `persistent_logins` VALUES ('UhpMjldLydaZLc9NLYXBsw==', 'sa', 'b/WwERX/47xgQbU4wM+eUw==', '2020-08-20 19:27:47');

-- ----------------------------
-- Table structure for sys_authority
-- ----------------------------
DROP TABLE IF EXISTS `sys_authority`;
CREATE TABLE `sys_authority` (
  `authority_id` varchar(255) NOT NULL,
  `authority_name` varchar(255) NOT NULL COMMENT 'Authority name, start with ''ROLE_'' and UPPERCASE',
  `authority_remark` varchar(255) NOT NULL COMMENT 'Description',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `authority_content` varchar(255) NOT NULL COMMENT 'accessible url, using comma to separate',
  PRIMARY KEY (`authority_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='System Authority Table';

-- ----------------------------
-- Records of sys_authority
-- ----------------------------
INSERT INTO `sys_authority` VALUES ('3fb1c570496d4c09ab99b8d31b06ccc', 'ROLE_USER', 'User', '2019-09-10 10:08:58', '2019-09-10 10:08:58', '');
INSERT INTO `sys_authority` VALUES ('3fb1c570496d4c09ab99b8d31b06xxx', 'ROLE_SA', 'Super Admin', '2019-09-10 10:08:58', '2019-09-10 10:08:58', '/sys/**,/logging');
INSERT INTO `sys_authority` VALUES ('3fb1c570496d4c09ab99b8d31b06zzz', 'ROLE_ADMIN', 'Admin', '2019-09-10 10:08:58', '2019-09-10 10:08:58', '/sys/**');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` varchar(255) NOT NULL,
  `menu_name` varchar(255) NOT NULL,
  `menu_path` varchar(255) NOT NULL,
  `menu_parent_id` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='System Menu';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('35cb950cebb04bb18bb1d8b742a02005', 'XXX', '/xxx', '', '2019-09-11 18:05:21', '2019-09-11 18:05:21');
INSERT INTO `sys_menu` VALUES ('35cb950cebb04bb18bb1d8b742a02xaa', 'Authority', '/sys/sysAuthority/authority', '35cb950cebb04bb18bb1d8b742a02xxx', '2019-09-10 10:08:58', '2019-09-10 10:08:58');
INSERT INTO `sys_menu` VALUES ('35cb950cebb04bb18bb1d8b742a02xcc', 'Menu', '/sys/sysMenu/menu', '35cb950cebb04bb18bb1d8b742a02xxx', '2019-09-10 10:08:58', '2019-09-10 10:08:58');
INSERT INTO `sys_menu` VALUES ('35cb950cebb04bb18bb1d8b742a02xxx', 'System', '/sys', '', '2019-09-10 10:08:58', '2019-09-10 10:08:58');
INSERT INTO `sys_menu` VALUES ('35cb950cebb04bb18bb1d8b742a02xzz', 'User', '/sys/sysUser/user', '35cb950cebb04bb18bb1d8b742a02xxx', '2019-09-10 10:08:58', '2019-09-10 10:08:58');
INSERT INTO `sys_menu` VALUES ('74315e162f524a4d88aa931f02416f26', 'Real-time Monitor', '/monitor', '35cb950cebb04bb18bb1d8b742a02xxx', '2020-06-10 15:07:07', '2020-06-10 15:07:07');
INSERT INTO `sys_menu` VALUES ('914aa22c78af4327822061f3eada4067', 'Real-time Logging', '/logging', '35cb950cebb04bb18bb1d8b742a02xxx', '2019-09-11 11:19:52', '2019-09-11 11:19:52');
INSERT INTO `sys_menu` VALUES ('bcf17dc0ce304f0ba02d64ce21ddb4f9', 'Settings', '/sys/sysSetting/setting', '35cb950cebb04bb18bb1d8b742a02xxx', '2019-09-17 10:46:11', '2019-09-17 10:46:11');

-- ----------------------------
-- Table structure for sys_setting
-- ----------------------------
DROP TABLE IF EXISTS `sys_setting`;
CREATE TABLE `sys_setting` (
  `id` varchar(255) NOT NULL COMMENT 'table id',
  `sys_name` varchar(255) DEFAULT NULL,
  `sys_logo` varchar(255) DEFAULT NULL,
  `sys_bottom_text` varchar(255) DEFAULT NULL,
  `sys_notice_text` longtext,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `user_init_password` varchar(255) DEFAULT NULL,
  `sys_color` varchar(255) DEFAULT NULL,
  `sys_api_encrypt` char(1) DEFAULT NULL COMMENT 'API Encryption (Y/N)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='System Settings';

-- ----------------------------
-- Records of sys_setting
-- ----------------------------
INSERT INTO `sys_setting` VALUES ('1', 'Base Admin', 'https://media1.popsugar-assets.com/files/thumbor/tse3EFaBN9bXiDx_ebXNFZH8624/fit-in/1024x1024/filters:format_auto-!!-:strip_icc-!!-/2020/02/18/734/n/24155406/addurlsyJ0rZ/i/Photos-Labrador-Puppies.jpg', '© 2019 - 2020  XXX System', '<h1 style=\"white-space: normal; text-align: center;\"><span style=\"color: rgb(255, 0, 0);\">Announcement</span></h1><p style=\"white-space: normal;\"><span style=\"color: rgb(255, 0, 0);\">Have a good day!</span></p><p style=\"white-space: normal;\"><br/></p><p><br/></p>', '2019-09-17 10:15:38', '2019-09-17 10:15:40', '123456', 'rgba(44, 102, 160, 0.73)', 'Y');

-- ----------------------------
-- Table structure for sys_shortcut_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_shortcut_menu`;
CREATE TABLE `sys_shortcut_menu` (
  `shortcut_menu_id` varchar(255) NOT NULL,
  `shortcut_menu_name` varchar(255) NOT NULL,
  `shortcut_menu_path` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `shortcut_menu_parent_id` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`shortcut_menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='User''s Shortcut Menu Table';

-- ----------------------------
-- Records of sys_shortcut_menu
-- ----------------------------
INSERT INTO `sys_shortcut_menu` VALUES ('104370a3fa7948bab156afd4a5f2a730', 'Customized Menu', '', '1', '', '2019-09-12 18:35:13', '2019-09-12 18:35:13');
INSERT INTO `sys_shortcut_menu` VALUES ('88353f04ad5d47b182c984bfbb1693cc', 'ggg', '/xxx', 'b5ac62e154964151a19c565346bb354a', '72d94b41b9994038bd2f2135a1de28d8', '2019-09-17 14:36:50', '2019-09-17 14:36:50');
INSERT INTO `sys_shortcut_menu` VALUES ('cf78ced9ce7b480c85812540d1936145', 'My Blog', 'https://xiaokeliu666.github.io/', '1', '104370a3fa7948bab156afd4a5f2a730', '2019-09-12 18:35:39', '2019-09-12 18:35:39');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` varchar(255) NOT NULL,
  `login_name` varchar(255) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `valid` char(1) NOT NULL COMMENT 'soft delete(Y/N)',
  `limited_ip` varchar(255) DEFAULT NULL,
  `expired_time` datetime DEFAULT NULL,
  `last_change_pwd_time` datetime NOT NULL,
  `limit_multi_login` char(1) NOT NULL COMMENT 'multi online (Y/N)',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='System User Table';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'sa', 'Super Admin', 'E10ADC3949BA59ABBE56E057F20F883E', 'Y', '', null, '2019-09-17 12:00:36', 'Y', '2019-07-19 16:36:03', '2019-09-17 12:00:36');
INSERT INTO `sys_user` VALUES ('2', 'admin', 'Admin', 'E10ADC3949BA59ABBE56E057F20F883E', 'Y', '', null, '2019-09-17 12:00:36', 'N', '2019-07-19 16:36:03', '2019-09-12 16:14:28');
INSERT INTO `sys_user` VALUES ('ffde3c986ff6448b8b34166ec2d071f8', 'scott', 'Scott', 'E10ADC3949BA59ABBE56E057F20F883E', 'Y', '', null, '2020-08-13 21:46:04', 'Y', '2020-08-13 21:46:04', '2020-08-18 20:08:52');

-- ----------------------------
-- Table structure for sys_user_authority
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_authority`;
CREATE TABLE `sys_user_authority` (
  `user_authority_id` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `authority_id` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`user_authority_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='User Authority Table';

-- ----------------------------
-- Records of sys_user_authority
-- ----------------------------
INSERT INTO `sys_user_authority` VALUES ('0dc1b156ed544c0986823e9cd818da08', '2', '3fb1c570496d4c09ab99b8d31b06ccc', '2019-09-12 16:14:28', '2019-09-12 16:14:28');
INSERT INTO `sys_user_authority` VALUES ('6e9d777f568b47f6a7e00b77c221b9d2', '37bb339bbd3440afb2ffd9ca2d8385bd', '3fb1c570496d4c09ab99b8d31b06ccc', '2020-08-13 21:48:48', '2020-08-13 21:48:48');
INSERT INTO `sys_user_authority` VALUES ('90c18739f3ad41ae8010f6c2b7eeaac5', '3fb1c570496d4c09ab99b8d31b0671cf', '3fb1c570496d4c09ab99b8d31b06ccc', '2019-09-17 12:09:47', '2019-09-17 12:09:47');
INSERT INTO `sys_user_authority` VALUES ('9ca34956ceae4af0a74f4931344e9d1b', '1', '3fb1c570496d4c09ab99b8d31b06ccc', '2019-09-17 12:00:37', '2019-09-17 12:00:37');
INSERT INTO `sys_user_authority` VALUES ('a414567aaae54b42b8344da02795cb91', '2', '3fb1c570496d4c09ab99b8d31b06zzz', '2019-09-12 16:14:28', '2019-09-12 16:14:28');
INSERT INTO `sys_user_authority` VALUES ('b34f7092406c46189fee2690d9f6e493', '1', '3fb1c570496d4c09ab99b8d31b06xxx', '2019-09-17 12:00:37', '2019-09-17 12:00:37');
INSERT INTO `sys_user_authority` VALUES ('de60e5bbbacf4c739e44a60130d0f534', 'b5ac62e154964151a19c565346bb354a', '3fb1c570496d4c09ab99b8d31b06ccc', '2019-09-17 14:28:58', '2019-09-17 14:28:58');
INSERT INTO `sys_user_authority` VALUES ('f6514b57d1524afd8dfa7cb2c3ca6a11', '1', '3fb1c570496d4c09ab99b8d31b06zzz', '2019-09-17 12:00:37', '2019-09-17 12:00:37');

-- ----------------------------
-- Table structure for sys_user_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_menu`;
CREATE TABLE `sys_user_menu` (
  `user_menu_id` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `menu_id` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`user_menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='User Menu Table';

-- ----------------------------
-- Records of sys_user_menu
-- ----------------------------
INSERT INTO `sys_user_menu` VALUES ('1337996a0aba460bbf0b82db9a1da207', '1', '35cb950cebb04bb18bb1d8b742a02xxx', '2020-06-10 15:07:23', '2020-06-10 15:07:23');
INSERT INTO `sys_user_menu` VALUES ('2b95cd9e21954bb69ae4ab4daf85a4ed', '37bb339bbd3440afb2ffd9ca2d8385bd', '914aa22c78af4327822061f3eada4067', '2020-08-13 21:48:48', '2020-08-13 21:48:48');
INSERT INTO `sys_user_menu` VALUES ('2f5383fbf56743ba990134c25d0e6f93', '37bb339bbd3440afb2ffd9ca2d8385bd', '35cb950cebb04bb18bb1d8b742a02xaa', '2020-08-13 21:48:48', '2020-08-13 21:48:48');
INSERT INTO `sys_user_menu` VALUES ('3232782f25ec44b09438ab9805b85f83', '2', '35cb950cebb04bb18bb1d8b742a02xcc', '2019-09-12 16:14:28', '2019-09-12 16:14:28');
INSERT INTO `sys_user_menu` VALUES ('39d6a157972e400fabcd753d3766efc9', '1', '35cb950cebb04bb18bb1d8b742a02xaa', '2020-06-10 15:07:23', '2020-06-10 15:07:23');
INSERT INTO `sys_user_menu` VALUES ('3b0a160e8c624b2f86918afcd5107704', '1', '35cb950cebb04bb18bb1d8b742a02xcc', '2020-06-10 15:07:23', '2020-06-10 15:07:23');
INSERT INTO `sys_user_menu` VALUES ('3de6f14bce3245e4a0dce5f1d11b774e', '37bb339bbd3440afb2ffd9ca2d8385bd', '35cb950cebb04bb18bb1d8b742a02xxx', '2020-08-13 21:48:48', '2020-08-13 21:48:48');
INSERT INTO `sys_user_menu` VALUES ('4b677be10460483181a1044eddaec3d4', '37bb339bbd3440afb2ffd9ca2d8385bd', '35cb950cebb04bb18bb1d8b742a02xzz', '2020-08-13 21:48:48', '2020-08-13 21:48:48');
INSERT INTO `sys_user_menu` VALUES ('5454e76620d248bdb4d403f7dccc13b1', '37bb339bbd3440afb2ffd9ca2d8385bd', '74315e162f524a4d88aa931f02416f26', '2020-08-13 21:48:48', '2020-08-13 21:48:48');
INSERT INTO `sys_user_menu` VALUES ('57791437b9774d8abf74562a49c55a1a', '2', '35cb950cebb04bb18bb1d8b742a02xxx', '2019-09-12 16:14:28', '2019-09-12 16:14:28');
INSERT INTO `sys_user_menu` VALUES ('6afadafdc36c426182853761bf68d870', '1', '74315e162f524a4d88aa931f02416f26', '2020-06-10 15:07:23', '2020-06-10 15:07:23');
INSERT INTO `sys_user_menu` VALUES ('6e8fe2b9307a4855ba7d006dc17c97ae', '3fb1c570496d4c09ab99b8d31b0671cf', '35cb950cebb04bb18bb1d8b742a02005', '2019-09-17 12:09:47', '2019-09-17 12:09:47');
INSERT INTO `sys_user_menu` VALUES ('81f4999dde514e0ea43acfc70bfd35a8', '1', '914aa22c78af4327822061f3eada4067', '2020-06-10 15:07:23', '2020-06-10 15:07:23');
INSERT INTO `sys_user_menu` VALUES ('9f8ccddc9fa84e0b9ff74128d20e9024', '2', '35cb950cebb04bb18bb1d8b742a02xaa', '2019-09-12 16:14:28', '2019-09-12 16:14:28');
INSERT INTO `sys_user_menu` VALUES ('c4220e4602fd4f2ca70da046466c6b45', '2', '35cb950cebb04bb18bb1d8b742a02xzz', '2019-09-12 16:14:28', '2019-09-12 16:14:28');
INSERT INTO `sys_user_menu` VALUES ('cdf8f786c658437ba77eb7d7fdd6b9cb', '1', '35cb950cebb04bb18bb1d8b742a02xzz', '2020-06-10 15:07:23', '2020-06-10 15:07:23');
INSERT INTO `sys_user_menu` VALUES ('d646090ba4114c85b0a2fc2c9082a188', '1', 'bcf17dc0ce304f0ba02d64ce21ddb4f9', '2020-06-10 15:07:23', '2020-06-10 15:07:23');
INSERT INTO `sys_user_menu` VALUES ('d8bfa6eb34ef4946bb2cd1b9c0dbac0d', 'b5ac62e154964151a19c565346bb354a', '35cb950cebb04bb18bb1d8b742a02005', '2019-09-17 14:28:58', '2019-09-17 14:28:58');
INSERT INTO `sys_user_menu` VALUES ('e43271e386f64c0f81e51fa3a2d0a017', '37bb339bbd3440afb2ffd9ca2d8385bd', '35cb950cebb04bb18bb1d8b742a02xcc', '2020-08-13 21:48:48', '2020-08-13 21:48:48');
INSERT INTO `sys_user_menu` VALUES ('e43c6fa7e7324d3395e5dac258b88e5b', '37bb339bbd3440afb2ffd9ca2d8385bd', 'bcf17dc0ce304f0ba02d64ce21ddb4f9', '2020-08-13 21:48:48', '2020-08-13 21:48:48');
