package topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class IOSLogSub {

    static Channel channel;
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();

        channel = connection.createChannel();

        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, TopicPub.TOPIC_LOG_EXCHANGE, TopicEnum.IOS_TOPIC.getV());
        channel.basicConsume(queueName, IOSLogSub::handle, tag -> {});
    }

    public static void handle(String consumerTag, Delivery delivery) throws IOException {
        long deliveryTag = delivery.getEnvelope().getDeliveryTag();
        System.out.println("#WX# : " + delivery.getEnvelope().getRoutingKey() + ", " + new String(delivery.getBody(), StandardCharsets.UTF_8));
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    }
}
