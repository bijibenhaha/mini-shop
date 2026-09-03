-- ============================================
-- seata 数据库: Seata Server 存储事务记录
-- 使用 db 模式时必需
-- ============================================
CREATE DATABASE IF NOT EXISTS seata DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE seata;

-- 全局事务表
CREATE TABLE IF NOT EXISTS global_table (
    xid VARCHAR(128) NOT NULL PRIMARY KEY,
    transaction_id BIGINT,
    status TINYINT NOT NULL,
    application_id VARCHAR(32),
    transaction_service_group VARCHAR(32),
    transaction_name VARCHAR(128),
    timeout INT,
    begin_time BIGINT,
    application_data VARCHAR(2000),
    gmt_create DATETIME,
    gmt_modified DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 分支事务表
CREATE TABLE IF NOT EXISTS branch_table (
    branch_id BIGINT NOT NULL PRIMARY KEY,
    xid VARCHAR(128) NOT NULL,
    transaction_id BIGINT,
    resource_group_id VARCHAR(32),
    resource_id VARCHAR(256),
    branch_type VARCHAR(8),
    status TINYINT,
    client_id VARCHAR(64),
    application_data VARCHAR(2000),
    gmt_create DATETIME(6),
    gmt_modified DATETIME(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 分布式锁表
CREATE TABLE IF NOT EXISTS distributed_lock (
    lock_key VARCHAR(128) NOT NULL PRIMARY KEY,
    lock_value VARCHAR(128) NOT NULL,
    expire_time BIGINT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 锁表
CREATE TABLE IF NOT EXISTS lock_table (
    row_key VARCHAR(128) NOT NULL,
    xid VARCHAR(128),
    transaction_id BIGINT,
    branch_id BIGINT NOT NULL,
    resource_id VARCHAR(256),
    table_name VARCHAR(32),
    pk VARCHAR(36),
    status TINYINT NOT NULL DEFAULT 0,
    gmt_create DATETIME,
    gmt_modified DATETIME,
    PRIMARY KEY (row_key, branch_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;