import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class Publisher {

    public static final String QUEUE_NAME = "FIRST";
    private static final AtomicInteger MSG_CNT = new AtomicInteger();

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        // 是否开启消息的处理结果回调监听
        boolean mandatory = true;

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            // 开启接收确认机制
            channel.confirmSelect();


            // 消息的接收结果回调
            ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
                // 是否一次确认多条
                if (multiple) {
                    System.out.println(Thread.currentThread().getName() + " confirm multiple " + deliveryTag);
                } else {
                    System.out.println(Thread.currentThread().getName() + " server confirm " + deliveryTag);
                }
            };
            ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
                System.out.println(Thread.currentThread().getName() + " server confirm " + deliveryTag);
            };
            channel.addConfirmListener(ackCallback, nackCallback);


            // 消息的分发失败结果回调
            channel.addReturnListener(retMsg -> {
                String msg = new String(retMsg.getBody(), StandardCharsets.UTF_8);
                System.out.printf("failed deliveries : %s, exchange = %s, code = %s, remark = %s \n",
                        msg, retMsg.getExchange(), retMsg.getReplyCode(), retMsg.getReplyText());
            });



            for (int i = 0; i < 20; i++) {
                String message = "Hello, i = " + MSG_CNT.incrementAndGet();
                long nextTag = channel.getNextPublishSeqNo();
                channel.basicPublish("", QUEUE_NAME, mandatory, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("sending to server " + nextTag + " : " + message);
            }

            // 等待一些回调
            Thread.sleep(1000 * 300);
        }


    }
}
