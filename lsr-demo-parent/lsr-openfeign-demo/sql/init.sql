CREATE TABLE `t_users` (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID，自动递增唯一标识',
  `first_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户的名字',
  `last_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户的姓氏',
  `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名，唯一且不能为空',
  `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户密码，使用加密存储',
  `email` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户邮箱，唯一且不能为空',
  `phone_number` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户电话号码，可为空',
  `address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户地址，可为空',
  `date_of_birth` date DEFAULT NULL COMMENT '用户出生日期，可为空',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '账户创建时间，默认为当前时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '账户更新时间，每次更新时自动修改为当前时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户信息表';


CREATE TABLE t_accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '账户ID，自动递增唯一标识',
    user_id INT NOT NULL COMMENT '用户ID，关联用户信息表',
    account_type ENUM('SAVINGS', 'CHECKING') NOT NULL COMMENT '账户类型，储蓄账户或支票账户',
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00 COMMENT '账户余额，默认为0.00',
    currency VARCHAR(3) NOT NULL DEFAULT 'USD' COMMENT '账户货币类型，默认为美元',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '账户创建时间，默认为当前时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '账户更新时间，每次更新时自动修改为当前时间',
    status ENUM('ACTIVE', 'INACTIVE', 'CLOSED') DEFAULT 'ACTIVE' COMMENT '账户状态，默认为活跃',
    FOREIGN KEY (user_id) REFERENCES t_users(user_id) ON DELETE CASCADE
) COMMENT='账户信息表';


