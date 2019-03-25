DROP TABLE IF EXISTS `admin_permission`;
CREATE TABLE `admin_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录id',
  `permission_key` varchar(8) DEFAULT NULL COMMENT '权限key',
  `permission_name` varchar(8) DEFAULT NULL COMMENT '权限',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4;

INSERT INTO `admin_permission` (`id`, `permission_key`, `permission_name`) VALUES ('1', 'GLKZ', 'GLKZ');
INSERT INTO `admin_permission` (`id`, `permission_key`, `permission_name`) VALUES ('2', 'CTKZ', '冲提控制');
INSERT INTO `admin_permission` (`id`, `permission_key`, `permission_name`) VALUES ('3', '用户控制', '用户控制');
INSERT INTO `admin_permission` (`id`, `permission_key`, `permission_name`) VALUES ('4', '币种控制', '币种控制');
INSERT INTO `admin_permission` (`id`, `permission_key`, `permission_name`) VALUES ('5', '众筹控制', '众筹控制');
INSERT INTO `admin_permission` (`id`, `permission_key`, `permission_name`) VALUES ('6', '交易控制', '交易控制');
INSERT INTO `admin_permission` (`id`, `permission_key`, `permission_name`) VALUES ('7', '理财控制', '理财控制');

DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '管理员id',
  `username` varchar(16) DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `status` tinyint(4) DEFAULT NULL COMMENT '管理员状态1有效 0无效',
  `nickname` varchar(32) DEFAULT NULL COMMENT '昵称',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `admin_type` tinyint(4) DEFAULT NULL COMMENT '管理员类型0超级管理员 1普通管理员',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4;

INSERT INTO `admin_user` (`id`, `username`, `password`, `status`, `nickname`, `created_at`, `updated_at`, `admin_type`) VALUES ('1', 'admin', '86f3059b228c8acf99e69734b6bb32cc', '1', '超级管理员', null, null, '0');

DROP TABLE IF EXISTS `admin_user_permission`;
CREATE TABLE `admin_user_permission` (
  `user_id` bigint(20) DEFAULT NULL COMMENT '管理员id',
  `permission_id` bigint(20) DEFAULT NULL COMMENT '权限id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `admin_user_permission` (`user_id`, `permission_id`) VALUES ('1', '1');
INSERT INTO `admin_user_permission` (`user_id`, `permission_id`) VALUES ('1', '2');
INSERT INTO `admin_user_permission` (`user_id`, `permission_id`) VALUES ('1', '3');
INSERT INTO `admin_user_permission` (`user_id`, `permission_id`) VALUES ('1', '4');
INSERT INTO `admin_user_permission` (`user_id`, `permission_id`) VALUES ('1', '5');
INSERT INTO `admin_user_permission` (`user_id`, `permission_id`) VALUES ('1', '6');
INSERT INTO `admin_user_permission` (`user_id`, `permission_id`) VALUES ('1', '7');

DROP TABLE IF EXISTS `admin_wallet`;
CREATE TABLE `admin_wallet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '钱包记录id',
  `is_hot` tinyint(4) DEFAULT NULL COMMENT '是否热钱包',
  `address` varchar(64) DEFAULT NULL COMMENT '钱包地址',
  `balance` decimal(40,20) DEFAULT NULL COMMENT '余额，保留字段',
  `block_type` tinyint(4) DEFAULT NULL COMMENT '区块链类型1ETH 2BTC',
  `pv_key` varchar(128) DEFAULT NULL COMMENT '热钱包私钥',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `app_channel`;
CREATE TABLE `app_channel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `channel_name` varchar(128) NOT NULL,
  `contact` text,
  `info` text,
  `created_at` bigint(20) DEFAULT NULL,
  `updated_at` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `app_info`;
CREATE TABLE `app_info` (
  `app_type` varchar(64) CHARACTER SET utf8mb4 NOT NULL COMMENT 'app类型， apk或ipa',
  `app_version` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'app版本号',
  `app_version_code` int(11) DEFAULT NULL COMMENT '内部版本号',
  `app_package` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '包名',
  `http_url` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '下载地址',
  PRIMARY KEY (`app_type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `app_message`;
CREATE TABLE `app_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `message` varchar(128) DEFAULT NULL COMMENT '消息内容',
  `content_id` bigint(20) DEFAULT NULL COMMENT '关联数据id',
  `content_type` varchar(16) DEFAULT NULL COMMENT '关联数据类型',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态0未确认 1确认中 2已确认 9取消/失败',
  `message_type` tinyint(4) DEFAULT NULL COMMENT '消息类型0普通消息 1推送消息',
  `is_read` tinyint(4) DEFAULT NULL COMMENT '已读状态',
  `send_flag` tinyint(4) DEFAULT NULL COMMENT '是否已发送',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  `user_id` bigint(20) NOT NULL COMMENT '用户时间',
  `push_time` bigint(20) DEFAULT NULL COMMENT '推送时间',
  `message_en` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`,`user_id`),
  KEY `index_message_1` (`created_at`) USING BTREE,
  KEY `index_message_2` (`content_id`,`content_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `app_order`;
CREATE TABLE `app_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单记录id',
  `order_number` varchar(32) DEFAULT NULL COMMENT '订单编号',
  `classify` tinyint(4) DEFAULT NULL COMMENT '订单种类[0区块链交易 1订单交易 2众筹交易（包含众筹和由众筹引起的释放）3划账 4理財]',
  `order_content_id` bigint(20) DEFAULT NULL COMMENT '关联记录id',
  `order_content_name` varchar(32) DEFAULT NULL COMMENT '关联内容名称',
  `token_id` bigint(20) DEFAULT NULL COMMENT '令牌id',
  `order_type` tinyint(4) DEFAULT NULL COMMENT '订单类型1转入 2转出',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '修改时间',
  `status` tinyint(4) DEFAULT NULL COMMENT '转账状态[0待打包 1确认中 2打包成功 9打包失败]',
  `value` decimal(40,20) DEFAULT NULL COMMENT '订单数量',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `hash` varchar(128) DEFAULT NULL COMMENT '交易hash',
  `from_address` varchar(64) DEFAULT NULL COMMENT '来源地址',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目id',
  `order_remark` varchar(32) DEFAULT NULL COMMENT '订单备注',
  `fee` decimal(40,20) DEFAULT NULL COMMENT '手续费',
  `to_address` varchar(128) DEFAULT NULL COMMENT '目标地址',
  `order_remark_en` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`,`user_id`),
  KEY `index_app_order_1` (`created_at`) USING BTREE,
  KEY `index_app_order_2` (`order_number`,`classify`) USING BTREE,
  KEY `index_app_order_3` (`token_id`) USING BTREE,
  KEY `index_app_order_4` (`hash`) USING BTREE,
  KEY `index_app_order_5` (`order_type`,`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `app_user`;
CREATE TABLE `app_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `cellphone` varchar(32) DEFAULT NULL COMMENT '手机号，冗余',
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  `head_image` varchar(128) DEFAULT NULL COMMENT '头像地址',
  `transaction_password` varchar(128) DEFAULT NULL COMMENT '交易密码',
  `nickname` varchar(32) DEFAULT NULL COMMENT '昵称',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `status` tinyint(4) DEFAULT NULL COMMENT '用户状态1有效0无效',
  `invite_level` int(20) DEFAULT NULL COMMENT '有效层级',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱地址',
  `pv_key` varchar(128) DEFAULT NULL COMMENT '私钥',
  `invite_num` int(11) DEFAULT NULL COMMENT '邀请数量',
  `salt` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_app_user_1` (`email`) USING BTREE,
  KEY `index_app_user_2` (`nickname`) USING BTREE,
  KEY `index_app_user_3` (`created_at`) USING BTREE,
  KEY `index_app_user_4` (`pv_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4;

INSERT INTO `app_user` (`id`, `cellphone`, `password`, `head_image`, `transaction_password`, `nickname`, `created_at`, `updated_at`, `status`, `invite_level`, `email`, `pv_key`, `invite_num`, `salt`)VALUES ('1', '18888888888', '3D3B23D929F4BE3A78A0399354F5D4A9', 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=538598390,4205429837&fm=27&gp=0.jpg', '3D3B23D929F4BE3A78A0399354F5D4A9', '1', '1547639892945', '1547639892945', '1', '1', 'bzt.vpay@gmail.com', '770d38a17c11a3a06f99ae35abf712da', '1', null);

DROP TABLE IF EXISTS `app_user_address`;
CREATE TABLE `app_user_address` (
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `token_id` bigint(20) DEFAULT NULL COMMENT '令牌id',
  `address` varchar(64) DEFAULT NULL COMMENT '地址'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `app_user_balance`;
CREATE TABLE `app_user_balance` (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `token_id` bigint(20) NOT NULL COMMENT '令牌id',
  `balance` decimal(40,20) DEFAULT NULL COMMENT '余额',
  `visible` tinyint(4) DEFAULT NULL COMMENT '是否可见',
  `pending_balance` decimal(40,20) DEFAULT NULL COMMENT '待确认金额，冗余',
  PRIMARY KEY (`user_id`,`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `app_user_invite`;
CREATE TABLE `app_user_invite` (
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `invite_user_id` bigint(20) DEFAULT NULL COMMENT '邀请用户id',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '更新时间',
  KEY `index_app_user_invite_1` (`user_id`) USING BTREE,
  KEY `index_app_user_invite_2` (`invite_user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `block_sign`;
CREATE TABLE `block_sign` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '签名记录id',
  `opr_type` tinyint(4) DEFAULT NULL COMMENT '操作类型0汇总 1充值 2提现',
  `order_id` varchar(64) DEFAULT NULL COMMENT '订单号',
  `sign` longtext COMMENT '签名内容',
  `result` varchar(255) DEFAULT NULL COMMENT '签名结果描述',
  `status` tinyint(4) DEFAULT NULL COMMENT '签名结果0等待 1已签名 9失败',
  `hash` varchar(128) DEFAULT NULL COMMENT '交易hash',
  `started_at` bigint(20) DEFAULT NULL COMMENT '开始时间',
  `token_type` varchar(16) DEFAULT NULL COMMENT '令牌类型, 如ETH或BTC',
  `contract_address` varchar(128) DEFAULT NULL COMMENT '合约地址',
  `from_address` varchar(64) DEFAULT NULL COMMENT '来源地址',
  `to_address` varchar(64) DEFAULT NULL COMMENT '目标地址',
  PRIMARY KEY (`id`),
  KEY `index_block_sign_1` (`started_at`) USING BTREE,
  KEY `index_block_sign_3` (`contract_address`) USING BTREE,
  KEY `index_block_sign_4` (`token_type`) USING BTREE,
  KEY `index_block_sign_5` (`hash`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `block_transaction`;
CREATE TABLE `block_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '区块链交易id',
  `hash` varchar(128) DEFAULT NULL COMMENT '交易hash',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `fee` decimal(40,20) DEFAULT NULL COMMENT '手续费',
  `height` int(11) DEFAULT NULL COMMENT '出块高度',
  `token_id` bigint(20) DEFAULT NULL COMMENT '令牌id',
  `token_type` varchar(32) DEFAULT NULL COMMENT '区块链类型如btc eth',
  `opr_type` tinyint(4) DEFAULT NULL COMMENT '操作类型0汇总1充值2提现',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `status` tinyint(4) DEFAULT NULL COMMENT ' 订单状态0打包中 1确认中 2确认完毕 9失败',
  `transaction_status` tinyint(4) DEFAULT NULL COMMENT '交易状态1. 待审核2. 待签名（审核通过后3. 拒绝4. 正在提币（导入签名文件后5. 提币成功（交易确认成功后6. 失败")',
  `error_msg` varchar(128) DEFAULT NULL COMMENT '错误原因',
  `error_data` varchar(128) DEFAULT NULL COMMENT '错误详情',
  `value` decimal(60,20) DEFAULT NULL COMMENT '数量',
  `from_address` varchar(64) DEFAULT NULL COMMENT '来源地址',
  `to_address` varchar(64) DEFAULT NULL COMMENT '目标地址',
  `order_number` varchar(128) DEFAULT NULL COMMENT '订单号',
  `plat_fee` decimal(40,20) DEFAULT NULL COMMENT '平台手续费',
  PRIMARY KEY (`id`),
  KEY `index_block_transaction_1` (`created_at`) USING BTREE,
  KEY `index_block_transaction_2` (`hash`) USING BTREE,
  KEY `index_block_transaction_3` (`token_id`,`token_type`,`user_id`) USING BTREE,
  KEY `index_block_transaction_4` (`from_address`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `block_usdt_withdraw_queue`;
CREATE TABLE `block_usdt_withdraw_queue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '提现签名记录地址',
  `order_id` varchar(32) DEFAULT NULL COMMENT '订单号',
  `from_address` varchar(128) DEFAULT NULL COMMENT '来源地址',
  `fee` decimal(40,20) DEFAULT NULL COMMENT '收付费',
  `to_address` varchar(128) DEFAULT NULL COMMENT '目标地址',
  `status` int(11) DEFAULT NULL COMMENT '状态0等待1确认中 2已确认 9失败',
  `value` decimal(40,20) DEFAULT NULL COMMENT '数量',
  `started_at` bigint(20) DEFAULT NULL COMMENT '开始时间',
  PRIMARY KEY (`id`),
  KEY `index_block_usdt_withdraw_queue_1` (`started_at`) USING BTREE,
  KEY `index_block_usdt_withdraw_queue_2` (`status`) USING BTREE,
  KEY `index_block_usdt_withdraw_queue_3` (`from_address`,`to_address`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `common_address`;
CREATE TABLE `common_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '地址记录id',
  `token_type` varchar(16) DEFAULT NULL COMMENT '区块链类型，eth或btc',
  `address` varchar(64) DEFAULT NULL COMMENT '地址',
  `used` tinyint(4) DEFAULT NULL COMMENT '是否被使用',
  `balance` decimal(40,20) DEFAULT NULL COMMENT '余额',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `address_type` varchar(16) DEFAULT NULL COMMENT '令牌名称',
  `approve` tinyint(4) DEFAULT NULL COMMENT '是否已运行approve方法（eth地址）',
  PRIMARY KEY (`id`),
  KEY `index_common_address_1` (`token_type`) USING BTREE,
  KEY `index_common_address_2` (`user_id`) USING BTREE,
  KEY `index_common_address_3` (`address_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `common_token`;
CREATE TABLE `common_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '令牌记录id',
  `token_name` varchar(32) DEFAULT NULL COMMENT '令牌名称',
  `token_cn_name` varchar(32) DEFAULT NULL COMMENT '令牌中文名称',
  `token_en_name` varchar(32) DEFAULT NULL COMMENT '令牌英文名称',
  `token_image` varchar(128) DEFAULT NULL COMMENT '令牌图片',
  `token_type` varchar(16) DEFAULT NULL COMMENT '令牌类型，如ETH BTC',
  `link` varchar(128) DEFAULT NULL COMMENT '区跨链浏览器地址，冗余',
  `token_decimal` int(11) DEFAULT NULL COMMENT '令牌位数（erc20）',
  `token_contract_address` varchar(64) DEFAULT NULL COMMENT '合约地址',
  `index_id` int(11) DEFAULT NULL COMMENT '排序位置，冗余',
  `visible` tinyint(4) DEFAULT NULL COMMENT '是否可见',
  `withdraw` tinyint(4) DEFAULT NULL COMMENT '是否可提现',
  `recharge` tinyint(4) DEFAULT NULL COMMENT '是否可充值',
  `fee` float DEFAULT NULL COMMENT '提现手续费',
  `transafer_fee` float DEFAULT NULL COMMENT '区块链实际手续费',
  `withdraw_min` decimal(40,20) DEFAULT NULL COMMENT '最小提现金额',
  `withdraw_max` decimal(40,20) DEFAULT NULL COMMENT '最大提现金额',
  `withdraw_day` decimal(40,20) DEFAULT NULL COMMENT '单日提现上限',
  `delete_status` tinyint(4) DEFAULT NULL COMMENT '删除标记位',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `hold` decimal(40,20) DEFAULT NULL COMMENT '汇总保留金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4;

INSERT INTO `common_token` (`id`, `token_name`, `token_cn_name`, `token_en_name`, `token_image`, `token_type`, `link`, `token_decimal`, `token_contract_address`, `index_id`, `visible`, `withdraw`, `recharge`, `fee`, `transafer_fee`, `withdraw_min`, `withdraw_max`, `withdraw_day`, `delete_status`, `created_at`, `updated_at`, `hold`) VALUES ('1', 'BZTB', 'BZTB', 'BZTB', 'https://ico-list.oss-cn-hangzhou.aliyuncs.com/cryptovalut/20190130143236EEtEfmYfQx.jpg', 'ETH', 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=538598390,4205429837&fm=27&gp=0.jpg', '18', '0xb17df9a3b09583a9bdcf757d6367171476d4d8a3', '0', '1', '1', '1', '100', '11', '0.00000000000000000000', '1000.00000000000000000000', '5000.00000000000000000000', '0', '0', '0', '0.00000000000000000000');
INSERT INTO `common_token` (`id`, `token_name`, `token_cn_name`, `token_en_name`, `token_image`, `token_type`, `link`, `token_decimal`, `token_contract_address`, `index_id`, `visible`, `withdraw`, `recharge`, `fee`, `transafer_fee`, `withdraw_min`, `withdraw_max`, `withdraw_day`, `delete_status`, `created_at`, `updated_at`, `hold`) VALUES ('2', 'BTC', '比特币', 'bitcoin', 'https://ico-list.oss-cn-hangzhou.aliyuncs.com/cryptovalut/20190130143329HEFer5ENdr.jpg', 'BTC', 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=538598390,4205429837&fm=27&gp=0.jpg', '0', '', null, '1', '1', '1', '10', '0.00005', '0.00000000000000000000', '1000.00000000000000000000', '5000.00000000000000000000', '0', '0', '0', '0.00000000000000000000');
INSERT INTO `common_token` (`id`, `token_name`, `token_cn_name`, `token_en_name`, `token_image`, `token_type`, `link`, `token_decimal`, `token_contract_address`, `index_id`, `visible`, `withdraw`, `recharge`, `fee`, `transafer_fee`, `withdraw_min`, `withdraw_max`, `withdraw_day`, `delete_status`, `created_at`, `updated_at`, `hold`) VALUES ('3', 'ETH', '以太坊', 'ethernum', 'https://ico-list.oss-cn-hangzhou.aliyuncs.com/cryptovalut/20190130143445Ad7YZHWBMs.png', 'ETH', 'www.baidu.com', '0', '', '2', '1', '1', '1', '0', '22', '0.00000000000000000000', '1000.00000000000000000000', '10000.00000000000000000000', '0', '0', '0', '0.00000000000000000000');
INSERT INTO `common_token` (`id`, `token_name`, `token_cn_name`, `token_en_name`, `token_image`, `token_type`, `link`, `token_decimal`, `token_contract_address`, `index_id`, `visible`, `withdraw`, `recharge`, `fee`, `transafer_fee`, `withdraw_min`, `withdraw_max`, `withdraw_day`, `delete_status`, `created_at`, `updated_at`, `hold`) VALUES ('4', 'USDT', '泰达币', 'USDT', 'https://ico-list.oss-cn-hangzhou.aliyuncs.com/cryptovalut/20190130143424DDb84B4yyG.png', 'BTC', 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=538598390,4205429837&fm=27&gp=0.jpg', '18', '', '3', '1', '1', '1', '0', '0.00005', '0.00000000000000000000', '1000.00000000000000000000', '5000.00000000000000000000', '0', '0', '0', '0.00000000000000000000');

DROP TABLE IF EXISTS `common_token_history`;
CREATE TABLE `common_token_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '价格记录历史id',
  `token_id` bigint(20) NOT NULL COMMENT '令牌id',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `price` decimal(40,20) DEFAULT NULL COMMENT '价格',
  PRIMARY KEY (`id`,`token_id`),
  KEY `index_common_token_history_1` (`created_at`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `common_token_price`;
CREATE TABLE `common_token_price` (
  `token_id` bigint(20) NOT NULL COMMENT '令牌id',
  `token_name` varchar(32) DEFAULT NULL COMMENT '令牌名称',
  `token_price` decimal(40,20) DEFAULT NULL COMMENT '令牌价格',
  PRIMARY KEY (`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `common_token_price` (`token_id`, `token_name`, `token_price`) VALUES ('1', 'BZTB', '1.00000000000000000000');
INSERT INTO `common_token_price` (`token_id`, `token_name`, `token_price`) VALUES ('2', '余额', '26011.31942273120825497240');
INSERT INTO `common_token_price` (`token_id`, `token_name`, `token_price`) VALUES ('3', 'ETH', '917.60720783197877864075');
INSERT INTO `common_token_price` (`token_id`, `token_name`, `token_price`) VALUES ('4', 'USDT', '6.82781503734650597537');

DROP TABLE IF EXISTS `token_volume`;
CREATE TABLE `token_volume` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '交易数量记录id',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `value` decimal(60,20) DEFAULT NULL COMMENT '数量',
  `token_id` bigint(20) NOT NULL COMMENT '令牌id',
  `used` tinyint(4) DEFAULT NULL COMMENT '是否已处理',
  PRIMARY KEY (`id`,`token_id`),
  KEY `index_token_volume_1` (`created_at`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4;

ALTER TABLE app_message PARTITION by HASH(user_id) PARTITIONS 256;
ALTER TABLE app_order PARTITION by HASH(user_id) PARTITIONS 256; 
ALTER TABLE token_volume PARTITION by HASH(token_id) PARTITIONS 256; 
ALTER TABLE app_user PARTITION by HASH(id) PARTITIONS 2; 
ALTER TABLE app_user_address PARTITION by HASH(user_id) PARTITIONS 8; 
ALTER TABLE app_user_balance PARTITION by HASH(user_id) PARTITIONS 8; 
ALTER TABLE app_user_invite PARTITION by HASH(user_id) PARTITIONS 256; 
ALTER TABLE block_sign PARTITION by HASH(id) PARTITIONS 32; 
ALTER TABLE block_usdt_withdraw_queue PARTITION by HASH(id) PARTITIONS 32; 
ALTER TABLE common_address PARTITION by HASH(id) PARTITIONS 64; 
ALTER TABLE common_token_history PARTITION by HASH(token_id) PARTITIONS 16;

DROP TABLE IF EXISTS `business_shop_payment`;
CREATE TABLE `business_shop_payment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shop_id` bigint(20) DEFAULT NULL,
  `payment_type` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `payment_account` varchar(128) DEFAULT NULL,
  `account_name` varchar(128) DEFAULT NULL,
  `payment_image` varchar(255) DEFAULT NULL,
  `bank` varchar(128) DEFAULT NULL,
  `branch` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `business_shop`;
CREATE TABLE `business_shop` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `updated_at` bigint(20) DEFAULT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `shop_name` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `business_transaction`;
CREATE TABLE `business_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_number` varchar(128) DEFAULT NULL,
  `self_order_number` varchar(128) DEFAULT NULL,
  `token_name` varchar(64) DEFAULT NULL,
  `token_id` bigint(20) DEFAULT NULL,
  `order_type` int(11) DEFAULT NULL,
  `order_status` int(11) DEFAULT NULL,
  `amount` decimal(10,0) DEFAULT NULL,
  `token_value` decimal(10,0) DEFAULT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `stop_at` bigint(20) DEFAULT NULL,
  `pay_at` bigint(20) DEFAULT NULL,
  `limit_time` bigint(20) DEFAULT NULL,
  `buy_user_id` bigint(20) DEFAULT NULL,
  `sell_user_id` bigint(20) DEFAULT NULL,
  `price` decimal(10,0) DEFAULT NULL,
  `pay_type` int(11) DEFAULT NULL,
  `auto_send` int(11) DEFAULT NULL,
  `pay_account` varchar(64) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `buy_username` varchar(64) DEFAULT NULL,
  `sell_username` varchar(64) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `updated_at` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `business_transaction`
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`, `user_id`),
DEFAULT CHARACTER SET DEFAULT;

ALTER TABLE `business_transaction`
MODIFY COLUMN `order_number`  varchar(128) CHARACTER SET utf8mb4 NULL DEFAULT NULL AFTER `id`,
MODIFY COLUMN `pay_account`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `pay_type`,
MODIFY COLUMN `buy_username`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `pay_account`,
MODIFY COLUMN `sell_username`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `buy_username`,
DEFAULT CHARACTER SET DEFAULT;

ALTER TABLE `app_user`
ADD COLUMN `is_businesses`  int NULL AFTER `salt`,
DEFAULT CHARACTER SET DEFAULT;

ALTER TABLE `app_user`
ADD COLUMN `is_proxy`  int NULL AFTER `is_businesses`,
DEFAULT CHARACTER SET DEFAULT;

ALTER TABLE `common_token`
ADD COLUMN `official_sell`  int NULL AFTER `hold`;

ALTER TABLE `business_shop`
ADD COLUMN `app_key`  varchar(64) NULL AFTER `shop_name`,
ADD COLUMN `app_secret`  varchar(64) NULL AFTER `app_key`;

ALTER TABLE `business_shop`
ADD COLUMN `callback_url`  varchar(255) NULL AFTER `app_secret`;

ALTER TABLE `business_transaction`
ADD COLUMN `remit_user_id`  int NULL AFTER `self_order_number`,
ADD COLUMN `remit_shop_id`  int NULL AFTER `remit_user_id`,
DEFAULT CHARACTER SET DEFAULT;

ALTER TABLE `business_shop_payment`
ADD COLUMN `user_id`  bigint NULL AFTER `payment_image`,
DEFAULT CHARACTER SET DEFAULT;
    private Integer hasBank;
    private Integer aliPaySwitch;
    private Integer hasAliPay;
    private Integer weChatSwitch;
    private Integer hasWeChat;

DROP TABLE IF EXISTS `business_supplier`;
CREATE TABLE `business_supplier` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `price_differences` float(10,5) DEFAULT NULL,
  `bank_switch` int(11) DEFAULT NULL,
  `has_bank` int(11) DEFAULT NULL,
  `ali_pay_switch` int(11) DEFAULT NULL,
  `has_ali_pay` int(11) DEFAULT NULL,
  `we_chat_switch` int(11) DEFAULT NULL,
  `has_we_chat` int(11) DEFAULT NULL
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
