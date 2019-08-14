package dien.com.pam_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    final String TAG = "dien_debug";

    Button btnAddTopic;
    ListView listTopic;
    ListView listMessage;

    static List<TopicSubcription> model = new ArrayList<>();
    TopicAdapter adapter = null;
    static List<MessageSubcription> messageModel = new ArrayList<>() ;
    static MessageAdapter msgAdapter = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listTopic = findViewById(R.id.lst_topic_id);
        adapter = new TopicAdapter();
        listTopic.setAdapter(adapter);

        listMessage = findViewById(R.id.lst_message);
        msgAdapter = new MessageAdapter();
        listMessage.setAdapter(msgAdapter);


        if (CameraActivity.txtTopicId != null) {
            addNewTopic();
        }

        btnAddTopic = findViewById(R.id.btn_add_topic);
        btnAddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomeActivity.this, CameraActivity.class);
                startActivity(myIntent);
            }
        });
    }

    void addNewTopic() {
        TopicSubcription t = new TopicSubcription();
        t.setTopicID(CameraActivity.txtTopicId);

        DownloadActivity dwn = new DownloadActivity();
        List<TopicSubcription> tpsub = dwn.getTopicSubcription();

        TopicSubcription topicSubcription = findUsingIterator(CameraActivity.txtTopicId, tpsub);
//        Toast.makeText(this, "user" + findUsingIterator(CameraActivity.txtTopicId, tpsub).getMqtt_user(), Toast.LENGTH_LONG).show();

        if (topicSubcription != null) {
            t.setTopicname(topicSubcription.getTopicname());
            t.setMqtt_user(topicSubcription.getMqtt_user());
            t.setMqtt_pass(topicSubcription.getMqtt_pass());
            adapter.add(t);

            MqttClient mqttClient = new MqttClient(getApplicationContext());
            //mqttClient.publishMessage(t.getTopicname());
            mqttClient.subscribeToTopic(t.getTopicname());
        }
        else {
            Toast.makeText(this, "Trùng mã thiế bị", Toast.LENGTH_LONG).show();
        }

    }


    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void addNewMessage(String topicName, String message) {
        Log.d(TAG, "addNewMessage: " +topicName + ":  " + message);
        MessageSubcription m = new MessageSubcription();
        m.setDate(getDateTime());
        m.setMsg(message);
        m.setTopicName(topicName);
        msgAdapter.add(m);
    }


    public TopicSubcription findUsingIterator(String topicID, List<TopicSubcription> topic) {
//        Toast.makeText(this, "find topicID" + topicID, Toast.LENGTH_SHORT).show();
        int i, n = topic.size();

        for (i = 0; i < n; i++) {
            if (topic.get(i).getTopicID().trim().equals(topicID.trim())) {
                //Toast.makeText(this, "return user " + topic.get(i).getMqtt_user(), Toast.LENGTH_SHORT).show();
                return topic.get(i);
            }
        }
        //Toast.makeText(this, "not found!", Toast.LENGTH_LONG).show();
        return null;
    }

    class TopicAdapter extends ArrayAdapter<TopicSubcription> {
        TopicAdapter() {
            super(HomeActivity.this, R.layout.topic_adapter, model);
        }

        public View getView(int position, View convertView,
                            ViewGroup parent) {
            View row = convertView;
            TopicHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();

                row = inflater.inflate(R.layout.topic_adapter, parent, false);
                holder = new TopicHolder(row);
                row.setTag(holder);
            } else {
                holder = (TopicHolder) row.getTag();
            }

            holder.populateFrom(model.get(position));

            return (row);
        }
    }

    class TopicHolder {
        private TextView topicID = null;
        private TextView topicName = null;
        private TextView topicUser = null;
        private TextView topicPass = null;

        TopicHolder(View row) {
            topicID = row.findViewById(R.id.txt_topic_id);
            topicName = row.findViewById(R.id.txt_topic_name);
            topicUser = row.findViewById(R.id.txt_mqtt_user);
            topicPass = row.findViewById(R.id.txt_mqtt_pass);
        }

        void populateFrom(TopicSubcription t) {
            topicID.setText(t.getTopicID());
            topicName.setText(t.getTopicname());
            topicUser.setText(t.getMqtt_user());
            topicPass.setText(t.getMqtt_pass());

        }
    }

    class MessageAdapter extends ArrayAdapter<MessageSubcription> {
        MessageAdapter() {
            super(HomeActivity.this, R.layout.message_adapter, messageModel);
        }

        public View getView(int position, View convertView,
                            ViewGroup parent) {
            View row = convertView;
            MessageHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();

                row = inflater.inflate(R.layout.message_adapter, parent, false);
                holder = new MessageHolder(row);
                row.setTag(holder);
            } else {
                holder = (MessageHolder) row.getTag();
            }

            holder.populateFrom(messageModel.get(position));

            return (row);
        }
    }

    class MessageHolder {
        private TextView txt_msg_time = null;
        private TextView txt_msg_recv = null;
        private TextView txt_msg_topicName = null;


        MessageHolder(View row) {
            txt_msg_time = row.findViewById(R.id.txt_msg_time);
            txt_msg_recv = row.findViewById(R.id.txt_msg_recv);
            txt_msg_topicName = row.findViewById(R.id.txt_msg_topicName);
        }

        void populateFrom(MessageSubcription m) {
            txt_msg_time.setText(m.getDate());
            txt_msg_recv.setText(m.getMsg());
            txt_msg_topicName.setText(m.getTopicName());
        }
    }
}
