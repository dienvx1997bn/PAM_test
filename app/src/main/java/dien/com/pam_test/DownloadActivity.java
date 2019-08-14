package dien.com.pam_test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

public class DownloadActivity extends AppCompatActivity {
    //Firebase var
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;

    private String UserID;

    private static List<TopicSubcription> list = new ArrayList<>();

    public List<TopicSubcription> getTopicSubcription() {
        return list;
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference();

        UserID = mFirebaseDatabase.push().getKey();

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    TopicSubcription tp = new TopicSubcription();
                    tp.setTopicID(ds.child("mqtt_clientID").getValue(String.class));
                    tp.setMqtt_user(ds.child("mqtt_user").getValue(String.class));
                    tp.setMqtt_pass(ds.child("mqtt_pwd").getValue(String.class));
                    tp.setTopicname("aqi/" + tp.getMqtt_user());
                    list.add(tp);
                    //Toast.makeText(DownloadActivity.this, tp.getMqtt_user() + tp.getMqtt_pass() + tp.getTopicID(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DownloadActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
            }
        });

        //Connect MQTT
        Context ct = getApplicationContext();

        MqttClient mqttClient = new MqttClient(getApplicationContext());
        mqttClient.connectMqtt();

        Intent intent = new Intent(this, HomeActivity.class);
        this.startActivity(intent);
    }




}
