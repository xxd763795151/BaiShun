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