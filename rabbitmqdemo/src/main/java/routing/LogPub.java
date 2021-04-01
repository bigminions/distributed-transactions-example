package routing;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class LogPub {

    public static final String LOG_EX = "log_ex";
    public static final String SPLIT = ":";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            boolean mandatory = true;
            setConfirmListener(channel);
            setReturnListener(channel);

            channel.exchangeDeclare(LOG_EX, BuiltinExchangeType.DIRECT);

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

                System.out.println("send to rabbitmq : " + LOG_EX + ":" + routingKey + ", msg = " + msg);
                channel.basicPublish(LOG_EX, routingKey, mandatory, null, msg.getBytes(StandardCharsets.UTF_8));

                Thread.sleep(1000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void setConfirmListener(Channel channel) throws IOException {
        channel.confirmSelect();

        ConfirmCallback ack = (m, t) -> {
            System.out.println("## ack from server " + m + ", t=" + t);
        };
        ConfirmCallback nack = (m, t) -> {
            System.out.println("## nack from server " + m + ", t=" + t);
        };
        channel.addConfirmListener(ack, nack);
    }

    static void setReturnListener(Channel channel) {
        channel.addReturnListener(r -> {
            System.out.println("## fail server return : " + new String(r.getBody(), StandardCharsets.UTF_8));
        });
    }
}
