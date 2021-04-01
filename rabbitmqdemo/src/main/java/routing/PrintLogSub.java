package routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class PrintLogSub implements Runnable {

    private static Channel channel;

    public static void main(String[] args) throws IOException, TimeoutException {
        new PrintLogSub().run();
    }

    public void handle(String consumerTag, Delivery delivery) throws IOException {
        long deliveryTag = delivery.getEnvelope().getDeliveryTag();
        System.out.println("#SCREEN# : " + delivery.getEnvelope().getRoutingKey() + ", " + new String(delivery.getBody(), StandardCharsets.UTF_8));
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    }

    @Override
    public void run() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            Connection connection = factory.newConnection();
            channel = connection.createChannel();

            boolean autoAck = false;
            String queueName = channel.queueDeclare().getQueue();

            // 绑定队列与exchange
            for (String rk : getRoutingKey()) {
                channel.queueBind(queueName, LogPub.LOG_EX, rk);
            }

            channel.basicConsume(queueName, autoAck, this::handle, consumerTag -> {});
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public String[] getRoutingKey() {
        return new String[]{"info", "error"};
    }

}
