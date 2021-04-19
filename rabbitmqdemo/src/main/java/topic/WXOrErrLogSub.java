package topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class WXOrErrLogSub {

    static Channel channel;
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();

        channel = connection.createChannel();

        String queueName = channel.queueDeclare().getQueue();

        // 这个消费者对两个topic都有兴趣，一个是微信topic，一个是错误日志topic
        channel.queueBind(queueName, TopicPub.TOPIC_LOG_EXCHANGE, TopicEnum.WX_TOPIC.getV());
        channel.queueBind(queueName, TopicPub.TOPIC_LOG_EXCHANGE, TopicEnum.ERR_LOG_TOPIC.getV());

        channel.basicConsume(queueName, WXOrErrLogSub::handle, tag -> {});
    }

    public static void handle(String consumerTag, Delivery delivery) throws IOException {
        long deliveryTag = delivery.getEnvelope().getDeliveryTag();
        System.out.println("#WX# : " + delivery.getEnvelope().getRoutingKey() + ", " + new String(delivery.getBody(), StandardCharsets.UTF_8));
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    }
}
