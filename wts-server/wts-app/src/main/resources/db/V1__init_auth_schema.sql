-- WTS V1: 认证与权限相关表 (MySQL 8.0, InnoDB, utf8mb4)
-- 从旧系统 wts.v1.4.1.sql 提取并转换

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------------------------------------
-- 用户表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `alone_auth_user`;
CREATE TABLE `alone_auth_user` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(64) NOT NULL COMMENT '姓名',
  `PASSWORD` varchar(128) NOT NULL COMMENT '密码(MD5旧密码或BCrypt新密码)',
  `COMMENTS` varchar(128) DEFAULT NULL COMMENT '备注',
  `TYPE` char(1) NOT NULL DEFAULT '1' COMMENT '用户类型:1系统用户,2其他,3超级管理员',
  `CTIME` varchar(14) NOT NULL COMMENT '创建时间(旧格式保留)',
  `UTIME` varchar(14) NOT NULL COMMENT '更新时间(旧格式保留)',
  `CUSER` varchar(32) NOT NULL COMMENT '创建人ID',
  `MUSER` varchar(32) NOT NULL COMMENT '修改人ID',
  `STATE` char(1) NOT NULL DEFAULT '1' COMMENT '状态:1正常,0禁用',
  `LOGINNAME` varchar(64) NOT NULL COMMENT '登录名',
  `LOGINTIME` varchar(14) DEFAULT NULL COMMENT '最后登录时间',
  `IMGID` varchar(32) DEFAULT NULL COMMENT '头像文件ID',
  `UUID` varchar(32) NOT NULL COMMENT 'UUID标识',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_LOGINNAME` (`LOGINNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 默认管理员账号 (密码 12345678，旧算法 MD5(password + loginname))
INSERT INTO `alone_auth_user` (`ID`, `NAME`, `PASSWORD`, `COMMENTS`, `TYPE`, `CTIME`, `UTIME`, `CUSER`, `MUSER`, `STATE`, `LOGINNAME`, `LOGINTIME`, `IMGID`, `UUID`) VALUES
('40288b854a329988014a329a12f30002', '系统管理员', '723D9BA0B38F5D39757D728BDA4903CB', '', '3', '20141210130925', '20260615120000', 'userId', '40288b854a329988014a329a12f30002', '1', 'sysadmin', '20260615120000', NULL, '40288b854a329988014a329a12f30002');

-- -----------------------------------------------------------
-- 权限定义表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `alone_auth_action`;
CREATE TABLE `alone_auth_action` (
  `ID` varchar(32) NOT NULL,
  `AUTHKEY` varchar(128) NOT NULL COMMENT '权限标识(如 user/list)',
  `NAME` varchar(64) NOT NULL COMMENT '权限名称',
  `COMMENTS` varchar(128) DEFAULT NULL COMMENT '备注',
  `CTIME` varchar(14) NOT NULL,
  `UTIME` varchar(14) NOT NULL,
  `CUSER` varchar(32) NOT NULL,
  `MUSER` varchar(32) NOT NULL,
  `STATE` char(1) NOT NULL DEFAULT '1',
  `CHECKIS` char(1) NOT NULL DEFAULT '1' COMMENT '是否检查:1是,0否',
  `LOGINIS` char(1) NOT NULL DEFAULT '1' COMMENT '是否需要登录:1是,0否',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限定义表';

-- -----------------------------------------------------------
-- 菜单/权限树表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `alone_auth_actiontree`;
CREATE TABLE `alone_auth_actiontree` (
  `ID` varchar(32) NOT NULL,
  `SORT` int NOT NULL DEFAULT 0 COMMENT '排序号',
  `PARENTID` varchar(32) NOT NULL DEFAULT 'NONE' COMMENT '父节点ID(NONE为根)',
  `NAME` varchar(64) NOT NULL COMMENT '节点名称',
  `TREECODE` varchar(256) NOT NULL COMMENT '树编码(全路径拼接)',
  `COMMENTS` varchar(128) DEFAULT NULL,
  `TYPE` char(1) NOT NULL COMMENT '类型:1分类,2菜单,3权限',
  `CTIME` varchar(14) NOT NULL,
  `UTIME` varchar(14) NOT NULL,
  `CUSER` varchar(32) NOT NULL,
  `UUSER` varchar(32) NOT NULL COMMENT '修改人ID',
  `STATE` char(1) NOT NULL DEFAULT '1',
  `ACTIONID` varchar(32) DEFAULT NULL COMMENT '关联的权限ID',
  `DOMAIN` varchar(64) NOT NULL DEFAULT 'wts' COMMENT '所属域',
  `ICON` varchar(64) DEFAULT NULL COMMENT '图标',
  `IMGID` varchar(32) DEFAULT NULL,
  `PARAMS` varchar(128) DEFAULT NULL COMMENT '附加参数',
  PRIMARY KEY (`ID`),
  KEY `IDX_PARENTID` (`PARENTID`),
  KEY `IDX_ACTIONID` (`ACTIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限树表';

-- -----------------------------------------------------------
-- 组织机构表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `alone_auth_organization`;
CREATE TABLE `alone_auth_organization` (
  `ID` varchar(32) NOT NULL,
  `TREECODE` varchar(256) NOT NULL COMMENT '树编码',
  `COMMENTS` varchar(128) DEFAULT NULL,
  `NAME` varchar(64) NOT NULL COMMENT '组织名称',
  `CTIME` varchar(14) NOT NULL,
  `UTIME` varchar(14) NOT NULL,
  `STATE` char(1) NOT NULL DEFAULT '1',
  `CUSER` varchar(32) NOT NULL,
  `MUSER` varchar(32) NOT NULL,
  `PARENTID` varchar(32) DEFAULT 'NONE' COMMENT '父组织ID',
  `SORT` int DEFAULT 0 COMMENT '排序号',
  `TYPE` char(1) NOT NULL DEFAULT '1' COMMENT '类型:1科室,2班组,3队组,0其他',
  `APPID` varchar(32) DEFAULT NULL COMMENT '关联的应用ID',
  `UUID` varchar(32) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_PARENTID` (`PARENTID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织机构表';

-- -----------------------------------------------------------
-- POP权限分配表 (人/组织/岗位 对 业务权限)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `alone_auth_pop`;
CREATE TABLE `alone_auth_pop` (
  `ID` varchar(32) NOT NULL,
  `POPTYPE` varchar(1) NOT NULL COMMENT '授权类型:1人,2组织,3岗位',
  `OID` varchar(32) NOT NULL COMMENT '授权对象ID',
  `ONAME` varchar(128) NOT NULL COMMENT '授权对象名称',
  `TARGETTYPE` varchar(64) NOT NULL COMMENT '目标业务类型',
  `TARGETID` varchar(32) NOT NULL COMMENT '目标业务ID',
  `TARGETNAME` varchar(128) DEFAULT NULL,
  `CTIME` varchar(16) NOT NULL,
  `CUSERNAME` varchar(64) NOT NULL,
  `CUSER` varchar(32) NOT NULL,
  `PSTATE` varchar(2) NOT NULL DEFAULT '1' COMMENT '状态:1有效',
  `PCONTENT` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_TARGET` (`TARGETTYPE`, `TARGETID`),
  KEY `IDX_OID` (`OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='POP权限分配表';

-- -----------------------------------------------------------
-- 岗位表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `alone_auth_post`;
CREATE TABLE `alone_auth_post` (
  `ID` varchar(32) NOT NULL,
  `CTIME` varchar(16) NOT NULL,
  `ETIME` varchar(16) NOT NULL COMMENT '修改时间',
  `CUSERNAME` varchar(64) NOT NULL,
  `CUSER` varchar(32) NOT NULL,
  `EUSERNAME` varchar(64) NOT NULL COMMENT '修改人姓名',
  `EUSER` varchar(32) NOT NULL COMMENT '修改人ID',
  `PSTATE` varchar(2) NOT NULL DEFAULT '1',
  `ORGANIZATIONID` varchar(32) DEFAULT NULL COMMENT '所属组织ID',
  `NAME` varchar(64) NOT NULL COMMENT '岗位名称',
  `EXTENDIS` varchar(2) NOT NULL DEFAULT '0' COMMENT '是否继承:0否,1是',
  `UUID` varchar(32) NOT NULL,
  `SOURCEID` varchar(64) DEFAULT NULL COMMENT '来源ID',
  PRIMARY KEY (`ID`),
  KEY `IDX_ORGID` (`ORGANIZATIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位表';

-- -----------------------------------------------------------
-- 岗位-菜单关联表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `alone_auth_postaction`;
CREATE TABLE `alone_auth_postaction` (
  `ID` varchar(32) NOT NULL,
  `MENUID` varchar(32) NOT NULL COMMENT '菜单ID(对应actiontree.ID)',
  `POSTID` varchar(32) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`ID`),
  KEY `IDX_MENUID` (`MENUID`),
  KEY `IDX_POSTID` (`POSTID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位菜单关联表';

-- -----------------------------------------------------------
-- 用户-组织关联表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `alone_auth_userorg`;
CREATE TABLE `alone_auth_userorg` (
  `ID` varchar(32) NOT NULL,
  `USERID` varchar(32) NOT NULL,
  `ORGANIZATIONID` varchar(32) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_USERID` (`USERID`),
  KEY `IDX_ORGID` (`ORGANIZATIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户组织关联表';

-- -----------------------------------------------------------
-- 用户-岗位关联表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `alone_auth_userpost`;
CREATE TABLE `alone_auth_userpost` (
  `ID` varchar(32) NOT NULL,
  `USERID` varchar(32) NOT NULL,
  `POSTID` varchar(32) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_USERID` (`USERID`),
  KEY `IDX_POSTID` (`POSTID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户岗位关联表';

-- -----------------------------------------------------------
-- 外部账户关联表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `alone_auth_outuser`;
CREATE TABLE `alone_auth_outuser` (
  `ID` varchar(32) NOT NULL,
  `CTIME` varchar(16) NOT NULL,
  `PSTATE` varchar(2) NOT NULL DEFAULT '1',
  `PCONTENT` varchar(128) DEFAULT NULL,
  `USERID` varchar(32) DEFAULT NULL COMMENT '关联的系统用户ID',
  `ACCOUNTID` varchar(64) NOT NULL COMMENT '外部账户ID',
  `ACCOUNTNAME` varchar(64) NOT NULL COMMENT '外部账户名',
  PRIMARY KEY (`ID`),
  KEY `IDX_USERID` (`USERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='外部账户关联表';

SET FOREIGN_KEY_CHECKS = 1;
