package topic;

public enum TopicEnum {
    WX_TOPIC("wx.*"),
    IOS_TOPIC("ios.*"),
    ERR_LOG_TOPIC("*.err");
    private String v;

    TopicEnum(String v) {
        this.v = v;
    }

    public String getV() {
        return v;
    }
}
