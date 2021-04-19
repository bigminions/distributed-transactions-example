package topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class WxLogSub {

    static Channel channel;
    static String WX_LOG_QUEUE = "WX_LOG_QUEUE";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();

        channel = connection.createChannel();
        // 定义一个微信队列
        channel.queueDeclare(WX_LOG_QUEUE, false, false, false, null);
        // 队列绑定到EXCHANGE和对应的routingKey
        channel.queueBind(WX_LOG_QUEUE, TopicPub.TOPIC_LOG_EXCHANGE, TopicEnum.WX_TOPIC.getV());

        channel.basicConsume(WX_LOG_QUEUE, WxLogSub::handle, tag -> {});
    }

    public static void handle(String consumerTag, Delivery delivery) throws IOException {
        long deliveryTag = delivery.getEnvelope().getDeliveryTag();
        System.out.println("#WX# : " + delivery.getEnvelope().getRoutingKey() + ", " + new String(delivery.getBody(), StandardCharsets.UTF_8));
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    }
}
