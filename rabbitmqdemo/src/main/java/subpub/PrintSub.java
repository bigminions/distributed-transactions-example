package subpub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 订阅： 打印日志
 */
public class PrintSub {

    private static Channel channel;

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        channel = connection.createChannel();


        boolean autoAck = false;


        String queueName = channel.queueDeclare().getQueue();
        // 绑定队列与exchange
        channel.queueBind(queueName, Pub.LOG_EXCHANGE, "");


        channel.basicConsume(queueName, autoAck, PrintSub::handle, consumerTag -> {});
    }

    static void handle(String consumerTag, Delivery delivery) throws IOException {
        long deliveryTag = delivery.getEnvelope().getDeliveryTag();
        System.out.println("#SCREEN# : " + deliveryTag + ", " + new String(delivery.getBody(), StandardCharsets.UTF_8));
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    }
}
