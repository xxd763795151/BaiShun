DROP TABLE IF EXISTS t_vip_users;
--  create vip users table;
CREATE TABLE t_vip_users (
  id VARCHAR(40) PRIMARY KEY NOT NULL,
  name VARCHAR(40) NOT NULL,
  money DECIMAL(18, 2) NOT NULL,
  tel VARCHAR(12) NOT NULL,
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);

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