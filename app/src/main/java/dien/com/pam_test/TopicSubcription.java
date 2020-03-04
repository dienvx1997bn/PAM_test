package dien.com.pam_test;

public class TopicSubcription {
    private String topicSN;
    private String topicAP;
    private String topicname;
    private String mqtt_user;
    private String mqtt_pass;

    public String getTopicAP() {
        return topicAP;
    }

    public void setTopicAP(String topicAP) {
        this.topicAP = topicAP;
    }

    public String getTopicSN() {
        return topicSN;
    }

    public void setTopicSN(String topicSN) {
        this.topicSN = topicSN;
    }

    public String getTopicname() {
        return topicname;
    }

    public void setTopicname(String topicname) {
        this.topicname = topicname;
    }

    public String getMqtt_user() {
        return mqtt_user;
    }

    public void setMqtt_user(String mqtt_user) {
        this.mqtt_user = mqtt_user;
    }

    public String getMqtt_pass() {
        return mqtt_pass;
    }

    public void setMqtt_pass(String mqtt_pass) {
        this.mqtt_pass = mqtt_pass;
    }
}
