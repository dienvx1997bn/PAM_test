package dien.com.pam_test;

public class MessageSubcription {
    private String date;
    private String topicName;
    private String topicID;
    private String temp_s;
    private String hum_s;
    private String pm25_s;
    private String pm10_s;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTemp_s() {
        return temp_s;
    }

    public void setTemp_s(String temp_s) {
        this.temp_s = temp_s;
    }

    public String getHum_s() {
        return hum_s;
    }

    public void setHum_s(String hum_s) {
        this.hum_s = hum_s;
    }

    public String getPm25_s() {
        return pm25_s;
    }

    public void setPm25_s(String pm25_s) {
        this.pm25_s = pm25_s;
    }

    public String getPm10_s() {
        return pm10_s;
    }

    public void setPm10_s(String pm10_s) {
        this.pm10_s = pm10_s;
    }


    public String getTopicID() {
        return topicID;
    }

    public void setTopicID(String topicID) {
        this.topicID = topicID;
    }
}
