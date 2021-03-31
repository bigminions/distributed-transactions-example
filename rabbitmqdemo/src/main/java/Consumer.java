import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer {

    private static final AtomicInteger HANDLE_CNT = new AtomicInteger();
    private static final AtomicInteger DONE_CNT = new AtomicInteger();
    private static Channel channel;

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        channel = connection.createChannel();

        // 是否开启自动回复ack
        boolean autoAck = false;
        channel.queueDeclare(Publisher.QUEUE_NAME, true, false, false, null);
        // 最多处理一条
        channel.basicQos(1);

        // basicConsume 在线程池内被动消费消息
        channel.basicConsume(Publisher.QUEUE_NAME, autoAck,
                Consumer::doWork,
                consumerTag -> {});

        // basicGet 当前线程主动获取消息
//        GetResponse gr1 = channel.basicGet(Publisher.QUEUE_NAME, false);
//        GetResponse gr2 = channel.basicGet(Publisher.QUEUE_NAME, false);
//        GetResponse gr3 = channel.basicGet(Publisher.QUEUE_NAME, false);


//        doWorkNack(gr1, gr2, gr3);

//        doWorkBulk(gr1, gr2);
    }

    /**
     * 消息处理
     */
    private static void doWork(String consumerTag, Delivery delivery) {
        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        long deliveryTag = delivery.getEnvelope().getDeliveryTag();

        try {
//            System.out.printf("PROCESSING --- %s, tag = %s, msg = %s \n", HANDLE_CNT.incrementAndGet(), deliveryTag, message);
            Thread.sleep(100);

            // 随机拒绝
            if (System.currentTimeMillis() % 2 == 0) {
                channel.basicReject(deliveryTag, true);
                System.out.printf("======================================================== REJECT msg = %s \n", message);
            } else {
                channel.basicAck(deliveryTag, false);
                System.out.printf("======================================================== DONE --- %s, msg = %s \n", DONE_CNT.incrementAndGet(), message);
            }

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 消息处理 这里直接批量拒绝
     */
    private static void doWorkNack(GetResponse ... getResponses) throws IOException {
        GetResponse last = null;
        for (GetResponse getResponse : getResponses) {
            if (getResponse == null) continue;
            String message = new String(getResponse.getBody(), StandardCharsets.UTF_8);

            System.out.printf("=== GET msg = %s\n", message);
            last = getResponse;
        }

        if (last != null)
            // 消息是否重入列，但这里没有入列次数
            if (last.getEnvelope().isRedeliver())
                channel.basicNack(last.getEnvelope().getDeliveryTag(), true, false);

            else
                channel.basicNack(last.getEnvelope().getDeliveryTag(), true, true);
    }

    /**
     * 消息处理，这里批量答复
     */
    private static void doWorkBulk(GetResponse ... getResponses) throws IOException {
        GetResponse last = null;
        for (GetResponse getResponse : getResponses) {
            if (getResponse == null) continue;
            String message = new String(getResponse.getBody(), StandardCharsets.UTF_8);

            System.out.printf("=== GET msg = %s \n", message);
            last = getResponse;
        }

        if (last != null)
            channel.basicAck(last.getEnvelope().getDeliveryTag(), true);
    }
}
