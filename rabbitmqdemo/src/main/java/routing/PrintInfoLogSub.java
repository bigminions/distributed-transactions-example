package routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class PrintInfoLogSub extends PrintLogSub {

    public static void main(String[] args) {
        new PrintInfoLogSub().run();
    }

    @Override
    public String[] getRoutingKey() {
        return new String[]{"info"};
    }
}
