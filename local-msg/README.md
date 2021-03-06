# 本地消息表demo

#### 三个系统 
- account 用户系统
- bill 账单系统
- wallet 钱包系统

> 使用本地消息表来实现一个业务逻辑： 
> > 支付一个升级账单 
> > > 1 钱包系统 - 钱包扣钱
> > > 2 账单系统 - 账单状态修改
> > > 3 用户系统 - 用户等级升级

涉及以下几个表
- wallet, wallet_local_msg
- bill
- account

为了方便演示，简化了业务逻辑和移除了service层。

假如用本地消息表来处理上述逻辑：
1. 在钱包系统中，修改钱包金额和写入本地表wallet_local_msg一起完成，写入的信息包括业务信息和发送主题
2. 读取刚保存的wallet_local_msg记录， 首次异步发送消息
3. 账单系统监听到消息后处理并反馈，用户系统收到消息后处理并反馈，收到所有系统的反馈后该消息视为成功消费，否则钱包系统的定时任务会定期捞出未消费成功的超时消息重发。

消息发送环节：
1. 除首次发送消息时，每次进入消息发送方法之前，即对数据的操作时间和重试次数做处理。
2. 当消息未消费成功，且最后一次处理跟当前时间已有一定间隔时，重新发送消息。
3. 在消费端需要实现幂等性，一个是消费过程中的幂等性，同一消息不能同时进入同类型的消费。一个是消费后的幂等性，即不重复消费消息。实现方式时消费前先入库（消费者的库）处理，并且保证同一消息不会重复入库。入库时记录下消息唯一id和消费状态，和消费线程等。