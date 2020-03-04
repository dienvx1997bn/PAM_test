package dien.com.pam_test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DownloadActivity extends AppCompatActivity {
//    //Firebase var
//    private FirebaseDatabase mFirebaseInstance;
//    private DatabaseReference mFirebaseDatabase;

    private String UserID;

    private static List<TopicSubcription> list = new ArrayList<>();

    public List<TopicSubcription> getTopicSubcription() {
        return list;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        readData();
//        mFirebaseInstance = FirebaseDatabase.getInstance();
//        mFirebaseDatabase = mFirebaseInstance.getReference();
//
//        UserID = mFirebaseDatabase.push().getKey();
//
//        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    TopicSubcription tp = new TopicSubcription();
//                    tp.setTopicSN(ds.child("mqtt_clientID").getValue(String.class));
//                    tp.setMqtt_user(ds.child("mqtt_user").getValue(String.class));
//                    tp.setMqtt_pass(ds.child("mqtt_pwd").getValue(String.class));
//                    tp.setTopicname("aqi/" + tp.getMqtt_user());
//                    list.add(tp);
//                    //Toast.makeText(DownloadActivity.this, tp.getMqtt_user() + tp.getMqtt_pass() + tp.getTopicSN(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(DownloadActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
//            }
//        });


        //Connect MQTT
        Context ct = getApplicationContext();

        MqttClient mqttClient = new MqttClient(getApplicationContext());
        mqttClient.connectMqtt();

        Intent intent = new Intent(this, HomeActivity.class);
        this.startActivity(intent);
    }

    private void readData() {
        InputStream is = getResources().openRawResource(R.raw.account);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using the comma as a separator).
                String[] tokens = line.split(",");

                TopicSubcription tp = new TopicSubcription();
                tp.setTopicSN(tokens[0]);
                tp.setTopicAP(tokens[1]);
                tp.setMqtt_user(tokens[2]);
                tp.setMqtt_pass(tokens[3]);
                tp.setTopicname("aqi/" + tp.getMqtt_user());
                list.add(tp);

            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
