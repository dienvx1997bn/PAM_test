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
