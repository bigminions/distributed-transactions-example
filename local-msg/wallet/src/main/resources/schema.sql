CREATE TABLE IF NOT EXISTS wallet (
    id INTEGER,
    user_id INTEGER,
    total decimal(20, 2),
    version INTEGER);

/**
  钱包本地消息表
 */
CREATE TABLE IF NOT EXISTS wallet_local_msg (
    id INTEGER NOT NULL AUTO_INCREMENT,
    buss_type INTEGER comment '业务类型 1活动业务，...',
    topic varchar(50) comment '消息主题',
    data varchar(255) comment '消息数据',
    retry_cnt integer(2) comment '重试次数，根据业务会提交人工处理',
    is_sent bool comment '是否发送成功',
    is_consumed bool comment '是否消费成功',
    last_send timestamp comment '最后一次发送时间, 超过最后发送时长一定时间仍未消费成功时重新发送到队列消费',
    create_time timestamp comment '消息创建时间',
    version INTEGER,
    PRIMARY KEY (`id`)
                                            );
