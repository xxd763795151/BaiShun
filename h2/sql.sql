DROP TABLE IF EXISTS t_vip_users;
--  create vip users table;
CREATE TABLE t_vip_users (
  id VARCHAR(40) PRIMARY KEY NOT NULL,
  name VARCHAR(40) NOT NULL,
  money DECIMAL(18, 2) NOT NULL,
  tel VARCHAR(12) NOT NULL,
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);
alter table t_vip_users add column type ENUM('complex', 'haircut');
alter table t_vip_users alter column type set default 0;
alter table t_vip_users add column remarks default ' ';
-- select * from t_vip_users;

-- create vip users info update record talbe;
DROP TABLE IF EXISTS t_user_info_updated_log;
CREATE TABLE t_user_info_updated_log (
  id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  user_id VARCHAR(40) NOT NULL,
  type ENUM('create', 'recharge', 'deduction', 'update') NOT NULL DEFAULT 0,
  log VARCHAR(255),
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
)

-- select * from t_user_info_updated_log;

update T_VIP_USERS  set type = null;
alter table T_VIP_USERS alter column type set data type enum('complex_card', 'haircut_card') ;
update T_VIP_USERS set type = 0;
update T_VIP_USERS set type = 1 where id like '%s%';
alter table t_vip_users alter column type set default 0;