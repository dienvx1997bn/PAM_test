package dien.com.pam_test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeActivity extends AppCompatActivity {

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("QR_scan");


    final String TAG = "dien_debug";
    public static TextView txt_connect_status;
    Button btnScan;
    ListView lstDevice;
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    static ArrayList<String> listItems = new ArrayList<String>();
    static ArrayList<String> lstTopicID = new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    static ArrayAdapter<String> adapter;


    String txtTopicIdCamera = "";

    static int counter = 0;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lstDevice = findViewById(R.id.lstDevice);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        lstDevice.setAdapter(adapter);


        Intent intent = getIntent();
        txtTopicIdCamera = intent.getStringExtra("txtTopicId"); //if it's a string you stored.

        final DownloadActivity dwn = new DownloadActivity();
        final List<TopicSubcription> tpsub = dwn.getTopicSubcription();

        if (txtTopicIdCamera != null) {
            if (lstTopicID.contains(txtTopicIdCamera)) {
                Toast.makeText(this, "Duplicate device", Toast.LENGTH_LONG).show();
            } else {
                lstTopicID.add(txtTopicIdCamera);
                TopicSubcription topicSubcription = findTopicSubcription(txtTopicIdCamera, tpsub);
                if (topicSubcription != null) {
                    counter = counter + 1;
                    listItems.add("" + counter + ".\t\t" + topicSubcription.getTopicSN());
                    adapter.notifyDataSetChanged();
                    try {
                        writeToFile(topicSubcription);
                    } catch (IOException e) {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Can not recognize device", Toast.LENGTH_LONG).show();
                }
            }

        }

        btnScan = findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(HomeActivity.this, CameraActivity.class);
                startActivity(myIntent);
            }
        });

    }


    @Override
    public void onBackPressed() {

    }


    private void writeToFile(TopicSubcription topicSubcription) throws IOException {

        myRef.child(getDay()).setValue(topicSubcription);

    }

    private String getDay() {
        DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd-HH-mm-ss");
        Date date = new Date();
        return dateFormat.format(date);
    }


    public TopicSubcription findTopicSubcription(String topicID, List<TopicSubcription> topic) {
//        Toast.makeText(this, "find topicID" + topicID, Toast.LENGTH_SHORT).show();
        int i, n = topic.size();

        for (i = 0; i < n; i++) {
            if (topic.get(i).getTopicSN().trim().equals(topicID.trim())) {
                //Toast.makeText(this, "return user " + topic.get(i).getMqtt_user(), Toast.LENGTH_SHORT).show();
                return topic.get(i);
            }
        }
        //Toast.makeText(this, "not found!", Toast.LENGTH_LONG).show();
        return null;
    }

    public String findTopicIDByTopicName(String topicName, List<TopicSubcription> topic) {
        int i, n = topic.size();

        for (i = 0; i < n; i++) {
            if ((topic.get(i).getTopicname().substring(4)).trim().equals(topicName.trim())) {
                //Toast.makeText(this, "return user " + topic.get(i).getMqtt_user(), Toast.LENGTH_SHORT).show();
                return topic.get(i).getTopicSN();
            }
        }
        //Toast.makeText(this, "not found!", Toast.LENGTH_LONG).show();
        return "";
    }


}
