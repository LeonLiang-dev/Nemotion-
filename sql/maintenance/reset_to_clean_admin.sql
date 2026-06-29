-- Manually reset an existing database to a clean administrator-only state.
-- WARNING: This deletes all business data, students, papers, subjects, rooms, cards, logs and demo data.
-- It keeps only the built-in sysadmin account and a default organization.

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP PROCEDURE IF EXISTS reset_to_clean_admin;

DELIMITER //
CREATE PROCEDURE reset_to_clean_admin()
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE current_table VARCHAR(128);
    DECLARE table_cursor CURSOR FOR
        SELECT TABLE_NAME
        FROM information_schema.TABLES
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_TYPE = 'BASE TABLE'
          AND TABLE_NAME NOT IN ('alone_auth_user', 'alone_auth_organization', 'alone_auth_userorg');
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    OPEN table_cursor;
    reset_loop: LOOP
        FETCH table_cursor INTO current_table;
        IF done = 1 THEN
            LEAVE reset_loop;
        END IF;
        SET @reset_sql = CONCAT('TRUNCATE TABLE `', current_table, '`');
        PREPARE reset_stmt FROM @reset_sql;
        EXECUTE reset_stmt;
        DEALLOCATE PREPARE reset_stmt;
    END LOOP;
    CLOSE table_cursor;
END//
DELIMITER ;

CALL reset_to_clean_admin();
DROP PROCEDURE reset_to_clean_admin;

DELETE FROM `alone_auth_userorg`;
DELETE FROM `alone_auth_user` WHERE `LOGINNAME` <> 'sysadmin';
DELETE FROM `alone_auth_organization`;

INSERT INTO `alone_auth_organization`
(`ID`, `TREECODE`, `COMMENTS`, `NAME`, `CTIME`, `UTIME`, `STATE`, `CUSER`, `MUSER`, `PARENTID`, `SORT`, `TYPE`, `APPID`, `UUID`)
VALUES
('00000000000000000000000000000001', '00000000000000000000000000000001', '系统默认组织', '默认组织', '20260629000000', '20260629000000', '1', '40288b854a329988014a329a12f30002', '40288b854a329988014a329a12f30002', 'NONE', 1, '0', NULL, '00000000000000000000000000000001')
ON DUPLICATE KEY UPDATE
`NAME` = VALUES(`NAME`),
`STATE` = VALUES(`STATE`),
`UTIME` = VALUES(`UTIME`),
`MUSER` = VALUES(`MUSER`);

INSERT INTO `alone_auth_user`
(`ID`, `NAME`, `PASSWORD`, `COMMENTS`, `TYPE`, `CTIME`, `UTIME`, `CUSER`, `MUSER`, `STATE`, `LOGINNAME`, `LOGINTIME`, `IMGID`, `UUID`)
VALUES
('40288b854a329988014a329a12f30002', '系统管理员', 'E5ABE6F0EBD707532E3D6C131F2E00F0', '', '3', '20260629000000', '20260629000000', '40288b854a329988014a329a12f30002', '40288b854a329988014a329a12f30002', '1', 'sysadmin', NULL, NULL, '40288b854a329988014a329a12f30002')
ON DUPLICATE KEY UPDATE
`NAME` = VALUES(`NAME`),
`PASSWORD` = VALUES(`PASSWORD`),
`TYPE` = VALUES(`TYPE`),
`STATE` = VALUES(`STATE`),
`UTIME` = VALUES(`UTIME`),
`MUSER` = VALUES(`MUSER`),
`LOGINTIME` = NULL,
`IMGID` = NULL;

INSERT INTO `alone_auth_userorg`
(`ID`, `USERID`, `ORGANIZATIONID`)
VALUES
('00000000000000000000000000000002', '40288b854a329988014a329a12f30002', '00000000000000000000000000000001')
ON DUPLICATE KEY UPDATE
`USERID` = VALUES(`USERID`),
`ORGANIZATIONID` = VALUES(`ORGANIZATIONID`);

SET FOREIGN_KEY_CHECKS = 1;
