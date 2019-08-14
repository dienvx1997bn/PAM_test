package dien.com.pam_test;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashMap;

public class MqttClient {

    public static HashMap<String, String> messageReceive = new HashMap<String, String>();

    final String TAG = "dien_debug";

    //MQTT config
    public static final String haihoaMQTT = "tcp://emq.emmasoft.com.vn:1883";
    public static final String testMQTT = "tcp://broker.hivemq.com:1883";
    public static final String testMQTT2 = "tcp://postman.cloudmqtt.com:14092";
//    public final String subscriptionTopic = "exampleAndroidPublishTopic";
//    public final String publishTopic = "exampleAndroidPublishTopic";
    public final String publishMessage = "Hello World!";
    private static final String USERNAME = "vuonrau";
    private static final String PASSWORD = "1122332211";
    private static MqttAndroidClient client;
    private String subTopic;
    Context ct;

    private String mqttMessage = null;

    public MqttClient(Context ct) {
        this.ct = ct;
    }

    public static MqttAndroidClient getClient() {
        return client;
    }

    public MqttAndroidClient connectMqtt() {
        //Connect to MQTT
        String clientId = org.eclipse.paho.client.mqttv3.MqttClient.generateClientId();
        client = new MqttAndroidClient(ct, haihoaMQTT, clientId);
        try {
            MqttConnectOptions options = new MqttConnectOptions();
//            options.setUserName("ejvoscut");
//            options.setPassword("2wCkJuRPDPgs".toCharArray());
            options.setUserName(USERNAME);
            options.setPassword(PASSWORD.toCharArray());
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(ct, "connecting success", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(ct, "connecting faild", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        return client;
    }

    public void subscribeToTopic(String subscriptionTopic) {
        Log.d(TAG, "Message:  " + subscriptionTopic);
        setSubTopic(subscriptionTopic);
        try {
            client.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //Toast.makeText(ct, "Subscribed!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    //Toast.makeText(ct, "Failed to subscribe", Toast.LENGTH_SHORT).show();
                }
            });

            client.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectionLost(Throwable throwable) {
                    Toast.makeText(ct, "The Connection was lost.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    String messageReceived = new String(mqttMessage.getPayload());
                    Log.d(TAG, "messageArrived: " + messageReceived);

//                Toast.makeText(ct, messageReceived, Toast.LENGTH_SHORT).show();
//                parseMqttMessage(new String(mqttMessage.getPayload()));
//                HomeActivity home = new HomeActivity();
//                home.addNewMessage(messageReceived);
                    addToList(getSubTopic(), messageReceived);
                    HomeActivity h = new HomeActivity();
                    h.addNewMessage(getSubTopic(), messageReceived);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    //Toast.makeText(ct, "Message delivered", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    if (reconnect) {
                        //Toast.makeText(ct,  "Reconnected to : " + serverURI, Toast.LENGTH_SHORT).show();
                        // Because Clean Session is true, we need to re-subscribe
                    } else {
                        //Toast.makeText(ct,  "Connected to : " + serverURI, Toast.LENGTH_SHORT).show();
                    }
                }
            }) ;

        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }


    }

    public void publishMessage(String mqttTopic) {

        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(publishMessage.getBytes());
            client.publish(mqttTopic, message);
            if (!client.isConnected()) {
                //Toast.makeText(ct, "client is not connected, messages in buffer.", Toast.LENGTH_SHORT).show();
            }
        } catch (MqttException e) {
            System.err.println("Error Publishing: " + e.getMessage());
            e.printStackTrace();
        }
    }






    public void addToList(String subscriptionTopic, String mqttMessage) {
        messageReceive.put(subscriptionTopic, mqttMessage);
    }


    public String getSubTopic() {
        return subTopic;
    }

    public void setSubTopic(String subTopic) {
        this.subTopic = subTopic;
    }
}
