package dien.com.pam_test;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;
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
    String haihoaMQTT = "ssl://emq.emmasoft.com.vn:8883";
    String USERNAME = "vuonrau";
    String PASSWORD = "1122332211";

    public static MqttAndroidClient client;
    public static MqttConnectOptions options;
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
        client = new MqttAndroidClient(ct, haihoaMQTT, "App_PAM_Test");
        try {
            options = new MqttConnectOptions();
            options.setUserName(USERNAME);
            options.setPassword(PASSWORD.toCharArray());
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(ct, "connecting success", Toast.LENGTH_LONG).show();
                    HomeActivity.connectStatus = "connecting success";
                    HomeActivity.txt_connect_status.setTextColor(Color.GREEN);
                    HomeActivity.txt_connect_status.setText("connect success");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(ct, "connecting faild", Toast.LENGTH_LONG).show();
                    HomeActivity.connectStatus = "connecting faild";
                    HomeActivity.txt_connect_status.setTextColor(Color.RED);
                    HomeActivity.txt_connect_status.setText("connect faild");
                }
            });

            options.setAutomaticReconnect(true);


        } catch (MqttException e) {
            e.printStackTrace();
        }

        return client;
    }

    public void unsubTopic(String topic) {
        try {
            IMqttToken unsubToken = client.unsubscribe(topic);
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The subscription could successfully be removed from the client
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // some error occurred, this is very unlikely as even if the client
                    // did not had a subscription to the topic the unsubscribe action
                    // will be successfully
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribeToTopic(String subscriptionTopic) {
        Log.d(TAG, "Message:  " + subscriptionTopic);
        setSubTopic(subscriptionTopic);
        try {
            client.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //Toast.makeText(ct, "Subscribed!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    //Toast.makeText(ct, "Failed to subscribe", Toast.LENGTH_SHORT).show();
                }
            });

//            client.setCallback(new MqttCallbackExtended() {
//                @Override
//                public void connectionLost(Throwable throwable) {
//                    Toast.makeText(ct, "The Connection was lost.", Toast.LENGTH_SHORT).show();
//                    HomeActivity.connectStatus = "lost connect";
//                    HomeActivity.txt_connect_status.setTextColor(Color.RED);
//                    HomeActivity.txt_connect_status.setText("lost connect");
//                }
//
//                @Override
//                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
//                    String messageReceived = new String(mqttMessage.getPayload());
//                    addToList(topic, messageReceived);
//                    HomeActivity h = new HomeActivity();
//                    h.addNewMessage(topic.substring(4), messageReceived);
//                }
//
//                @Override
//                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//                    //Toast.makeText(ct, "Message delivered", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void connectComplete(boolean reconnect, String serverURI) {
//                    if (reconnect) {
//                        //Toast.makeText(ct,  "Reconnected to : " + serverURI, Toast.LENGTH_SHORT).show();
//                        // Because Clean Session is true, we need to re-subscribe
//                    } else {
//                        //Toast.makeText(ct,  "Connected to : " + serverURI, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }) ;

        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
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
