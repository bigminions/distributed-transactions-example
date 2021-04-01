package routing;

public class PrintErrorLogSub extends PrintLogSub {

    public static void main(String[] args) {
        new PrintErrorLogSub().run();
    }

    @Override
    public String[] getRoutingKey() {
        return new String[]{"error"};
    }
}
