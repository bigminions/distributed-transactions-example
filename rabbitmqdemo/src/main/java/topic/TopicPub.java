package topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class TopicPub {

    public static final String TOPIC_LOG_EXCHANGE = "topic_log";
    public static final String SPLIT = ":";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            boolean mandatory = true;
            setConfirmListener(channel);
            setReturnListener(channel);

            channel.exchangeDeclare(TOPIC_LOG_EXCHANGE, BuiltinExchangeType.TOPIC);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Please input routingKey and msg ,eg : info : try to start game, (Q to quit)");
                String arrStr = scanner.nextLine();

                if ("Q".equalsIgnoreCase(arrStr)) {
                    break;
                }
                if (arrStr == null || !arrStr.contains(SPLIT)) {
                    continue;
                }

                String routingKey = arrStr.substring(0, arrStr.indexOf(SPLIT)).trim();
                String msg = arrStr.substring(arrStr.indexOf(SPLIT) + 1).trim();

                System.out.println("send to rabbitmq : " + TOPIC_LOG_EXCHANGE + ":" + routingKey + ", msg = " + msg);
                channel.basicPublish(TOPIC_LOG_EXCHANGE, routingKey, mandatory, null, msg.getBytes(StandardCharsets.UTF_8));

                Thread.sleep(1000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void setConfirmListener(Channel channel) throws IOException {
        channel.confirmSelect();

        ConfirmCallback ack = (m, t) -> {
            System.out.println("ack from server " + m + ", t=" + t);
        };
        ConfirmCallback nack = (m, t) -> {
            System.out.println("nack from server " + m + ", t=" + t);
        };
        channel.addConfirmListener(ack, nack);
    }

    static void setReturnListener(Channel channel) {
        channel.addReturnListener(r -> {
            System.out.println("fail server return : " + new String(r.getBody(), StandardCharsets.UTF_8));
        });
    }
}
