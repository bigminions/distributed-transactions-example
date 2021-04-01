package subpub;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

public class Pub {

    public static final String LOG_EXCHANGE = "log";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {

            boolean mandatory = true;
            channel.confirmSelect();
            setConfirmListener(channel);
            setReturnListener(channel);

            channel.exchangeDeclare(LOG_EXCHANGE, BuiltinExchangeType.FANOUT);

            for (int i = 0; i < 10; i++) {
                Thread.sleep(100);

                String log = "Info log at " + LocalDateTime.now() + ", tag = " + channel.getNextPublishSeqNo();
                channel.basicPublish(LOG_EXCHANGE, "", mandatory, null, log.getBytes(StandardCharsets.UTF_8));
            }

            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void setConfirmListener(Channel channel) {
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
