/*
SQLyog Enterprise - MySQL GUI v8.14 
MySQL - 5.5.30 : Database - fable_outer_test
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`fable_outer_test` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `fable_outer_test`;

/*Table structure for table `dsp_ftp_mapping` */

DROP TABLE IF EXISTS `dsp_ftp_mapping`;

CREATE TABLE `dsp_ftp_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '无意义主键',
  `INNER_ADDRESS` varchar(255) DEFAULT NULL COMMENT '内交换地址',
  `INNER_USERNAME` varchar(32) DEFAULT NULL COMMENT '内交换用户名称',
  `OUTER_ADDRESS` varchar(255) DEFAULT NULL COMMENT '外交换地址',
  `OUTER_USERNAME` varchar(32) DEFAULT NULL COMMENT '外交换用户名称',
  `USER_FLAG` char(1) NOT NULL COMMENT '1:由内交换到外 0:由外交换到内',
  `CREATE_USER` int(11) DEFAULT NULL COMMENT '创建用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_USER` int(11) DEFAULT NULL COMMENT '更新用户',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `DESCRIPTION` varchar(1000) DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Data for the table `dsp_ftp_mapping` */

/*Table structure for table `ftp_user` */

DROP TABLE IF EXISTS `ftp_user`;

CREATE TABLE `ftp_user` (
  `userid` varchar(64) NOT NULL COMMENT '用户ID',
  `userpassword` varchar(64) DEFAULT NULL COMMENT '用户密码',
  `homedirectory` varchar(128) NOT NULL COMMENT '用户目录',
  `enableflag` tinyint(1) DEFAULT '1' COMMENT '启用标志，1启用，0不启用',
  `writepermission` tinyint(1) DEFAULT '1' COMMENT '写权限，1有写权限，0没有写权限',
  `idletime` int(11) DEFAULT '600000' COMMENT '最低空闲时间',
  `uploadrate` int(11) DEFAULT '1000' COMMENT '上传速率',
  `downloadrate` int(11) DEFAULT '1000' COMMENT '下载速率',
  `maxloginnumber` int(11) DEFAULT '40' COMMENT '最多登录个数，默认为40',
  `maxloginperip` int(11) DEFAULT '40' COMMENT '最多登录IP，默认为40',
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ftp_user` */

insert  into `ftp_user`(`userid`,`userpassword`,`homedirectory`,`enableflag`,`writepermission`,`idletime`,`uploadrate`,`downloadrate`,`maxloginnumber`,`maxloginperip`) values ('qiushuang','qiushuang','/root/qiushuang',0,1,600000,1000,1000,40,40),('sss','sss','/root/root/path',0,1,600000,1000,1000,40,40),('wuhao','wuhao','/root/path',0,1,600000,1000,1000,40,40);

/*Table structure for table `sys_id_gen` */

DROP TABLE IF EXISTS `sys_id_gen`;

CREATE TABLE `sys_id_gen` (
  `GEN_KEY` varchar(255) DEFAULT NULL,
  `GEN_VALUE` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_id_gen` */

insert  into `sys_id_gen`(`GEN_KEY`,`GEN_VALUE`) values ('SYS_USER_ID',13),('SYS_ROLE_ID',13),('SYS_MENU_ID',24),('SYS_RESOURCE_ID',15);

/*Table structure for table `sys_menu_info` */

DROP TABLE IF EXISTS `sys_menu_info`;

CREATE TABLE `sys_menu_info` (
  `id` bigint(20) NOT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `CREATE_USER` varchar(16) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `UPDATE_USER` varchar(16) DEFAULT NULL,
  `DEL_FLAG` varchar(1) DEFAULT NULL,
  `DESCRIPTION` longtext,
  `ICON_URL` varchar(128) DEFAULT NULL,
  `MENU_NAME` varchar(32) DEFAULT NULL,
  `SORT_ORDER` varchar(8) DEFAULT NULL,
  `URL` varchar(128) DEFAULT NULL,
  `PID` bigint(20) DEFAULT NULL,
  `MENU_LEVEL` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3B7A147CA4BE80D2` (`PID`),
  CONSTRAINT `FK3B7A147CA4BE80D2` FOREIGN KEY (`PID`) REFERENCES `sys_menu_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_menu_info` */

insert  into `sys_menu_info`(`id`,`CREATE_TIME`,`CREATE_USER`,`UPDATE_TIME`,`UPDATE_USER`,`DEL_FLAG`,`DESCRIPTION`,`ICON_URL`,`MENU_NAME`,`SORT_ORDER`,`URL`,`PID`,`MENU_LEVEL`) values (1,NULL,NULL,'2014-05-23 17:10:05','1','0','','menu_nav','导航菜单','1',NULL,NULL,1),(2,NULL,NULL,'2014-04-22 19:09:26','9','0','','menu_sysManage','权限管理','1',NULL,1,2),(3,NULL,NULL,'2014-05-23 17:18:13','10','0','用户管理','menu_sysManage_user','用户管理','1','/userInfo/maintain',2,3),(4,NULL,NULL,'2014-05-23 17:10:53','1','0','角色管理','menu_sysManage_role','角色管理','1','/roleInfo/maintain',2,3),(13,'2014-04-08 12:40:28','1','2014-05-23 17:18:19','10','0','菜单管理','menu_sysManage_menu','菜单管理','1','/menuInfo/maintain',2,3);

/*Table structure for table `sys_menu_res` */

DROP TABLE IF EXISTS `sys_menu_res`;

CREATE TABLE `sys_menu_res` (
  `MENU_ID` bigint(20) NOT NULL,
  `RESOURCE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`MENU_ID`,`RESOURCE_ID`),
  KEY `FK860C52F2DD623B42` (`MENU_ID`),
  KEY `FK860C52F2B44C3C62` (`RESOURCE_ID`),
  CONSTRAINT `FK860C52F2B44C3C62` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `sys_resource_info` (`id`),
  CONSTRAINT `FK860C52F2DD623B42` FOREIGN KEY (`MENU_ID`) REFERENCES `sys_menu_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_menu_res` */

insert  into `sys_menu_res`(`MENU_ID`,`RESOURCE_ID`) values (3,7),(4,11),(13,13);

/*Table structure for table `sys_resource_info` */

DROP TABLE IF EXISTS `sys_resource_info`;

CREATE TABLE `sys_resource_info` (
  `id` bigint(20) NOT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `CREATE_USER` varchar(16) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `UPDATE_USER` varchar(16) DEFAULT NULL,
  `DEL_FLAG` varchar(1) DEFAULT NULL,
  `DESCRIPTION` longtext,
  `ENABLED` varchar(1) DEFAULT NULL,
  `RES_NAME` varchar(32) DEFAULT NULL,
  `RES_TYPE` varchar(2) DEFAULT NULL,
  `URL` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `URL` (`URL`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_resource_info` */

insert  into `sys_resource_info`(`id`,`CREATE_TIME`,`CREATE_USER`,`UPDATE_TIME`,`UPDATE_USER`,`DEL_FLAG`,`DESCRIPTION`,`ENABLED`,`RES_NAME`,`RES_TYPE`,`URL`) values (7,'2014-04-02 15:01:07','1','2014-04-23 16:02:46','9','0','用户资源',NULL,'用户资源','','/userInfo/**'),(11,'2014-04-03 14:45:55','1','2014-04-11 16:31:07','1','0','角色资源',NULL,'角色资源','','/roleInfo/**'),(13,'2014-04-11 16:32:52','1','2014-05-23 17:11:04','1','0','',NULL,'菜单资源','','/menuInfo/**'),(14,'2014-04-23 15:53:20','1',NULL,NULL,'0','',NULL,'主页','','/main.jsp');

/*Table structure for table `sys_role_info` */

DROP TABLE IF EXISTS `sys_role_info`;

CREATE TABLE `sys_role_info` (
  `id` bigint(20) NOT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `CREATE_USER` varchar(16) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `UPDATE_USER` varchar(16) DEFAULT NULL,
  `DEL_FLAG` varchar(1) DEFAULT NULL,
  `DESCRIPTION` longtext,
  `ROLE_NAME` varchar(32) NOT NULL,
  `menuNames` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ROLE_NAME` (`ROLE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_role_info` */

insert  into `sys_role_info`(`id`,`CREATE_TIME`,`CREATE_USER`,`UPDATE_TIME`,`UPDATE_USER`,`DEL_FLAG`,`DESCRIPTION`,`ROLE_NAME`,`menuNames`) values (1,NULL,NULL,'2014-05-23 17:17:54','10','0','管理员','管理员',NULL),(3,'2014-03-27 19:17:05','1','2014-05-23 17:17:59','10','0','操作员','操作员',NULL),(12,'2014-05-23 17:11:38','1','2014-05-23 17:11:45','1','0','超级管理员','超级管理员',NULL);

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `ROLE_ID` bigint(20) NOT NULL,
  `MENU_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ROLE_ID`,`MENU_ID`),
  KEY `FKAA83B076DD623B42` (`MENU_ID`),
  KEY `FKAA83B076AE76C62` (`ROLE_ID`),
  CONSTRAINT `FKAA83B076AE76C62` FOREIGN KEY (`ROLE_ID`) REFERENCES `sys_role_info` (`id`),
  CONSTRAINT `FKAA83B076DD623B42` FOREIGN KEY (`MENU_ID`) REFERENCES `sys_menu_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_role_menu` */

insert  into `sys_role_menu`(`ROLE_ID`,`MENU_ID`) values (1,3),(12,3),(12,4),(12,13);

/*Table structure for table `sys_user_info` */

DROP TABLE IF EXISTS `sys_user_info`;

CREATE TABLE `sys_user_info` (
  `id` bigint(20) NOT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `CREATE_USER` varchar(16) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `UPDATE_USER` varchar(16) DEFAULT NULL,
  `DEL_FLAG` varchar(1) DEFAULT NULL,
  `DESCRIPTION` longtext,
  `PASSWORD` varchar(32) DEFAULT NULL,
  `REAL_NAME` varchar(32) DEFAULT NULL,
  `USERNAME` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `USERNAME` (`USERNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_user_info` */

insert  into `sys_user_info`(`id`,`CREATE_TIME`,`CREATE_USER`,`UPDATE_TIME`,`UPDATE_USER`,`DEL_FLAG`,`DESCRIPTION`,`PASSWORD`,`REAL_NAME`,`USERNAME`) values (1,NULL,NULL,'2014-05-23 17:16:41','10','0','管理员','52d04dc20036dbd8','汪朝','admin'),(11,'2014-05-23 17:15:25','10','2014-05-23 17:16:17','10','0','系统管理员','52d04dc20036dbd8','系统管理员','sys'),(12,'2014-05-23 17:16:04','10',NULL,NULL,'0','操作员','52d04dc20036dbd8','操作员','fable');

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `USER_ID` bigint(20) NOT NULL,
  `ROLE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`USER_ID`,`ROLE_ID`),
  KEY `FKAABB7D58AE76C62` (`ROLE_ID`),
  KEY `FKAABB7D58B0127DC2` (`USER_ID`),
  CONSTRAINT `FKAABB7D58AE76C62` FOREIGN KEY (`ROLE_ID`) REFERENCES `sys_role_info` (`id`),
  CONSTRAINT `FKAABB7D58B0127DC2` FOREIGN KEY (`USER_ID`) REFERENCES `sys_user_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_user_role` */

insert  into `sys_user_role`(`USER_ID`,`ROLE_ID`) values (1,1),(12,3),(11,12);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
