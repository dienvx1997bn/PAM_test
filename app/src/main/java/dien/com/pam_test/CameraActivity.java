package dien.com.pam_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;


public class CameraActivity extends AppCompatActivity {

    public static String txtTopicId = null;

    TextView txt_add_manual;
    Button buttonScan;
    Button btn_add_manual;

    //qr code scanner object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        txt_add_manual = findViewById(R.id.txt_add_manual);
        txt_add_manual.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);


        buttonScan = findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
            }
        });

        btn_add_manual = findViewById(R.id.btn_add_manual);


        //intializing scan object
        qrScan = new IntentIntegrator(this);
        qrScan.setTimeout(10000);
        qrScan.setPrompt("Scan QR code");

    }

    public void addManual(View v) {
        if (v == btn_add_manual) {
            txtTopicId = txt_add_manual.getText().toString();

            if (checkHave(txtTopicId)) {
                Intent intent = new Intent(this, HomeActivity.class);
                this.startActivity(intent);
            } else {
                Toast.makeText(this, "Can not recognize device", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onBackPressed() {
        txtTopicId = null;
        Intent intent = new Intent(this, HomeActivity.class);
        this.startActivity(intent);
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && resultCode == RESULT_OK) {
            String[] lines = result.getContents().split("\r?\n");

            txtTopicId = lines[1];
            if (txtTopicId.startsWith("S/N:")) {
                txtTopicId = txtTopicId.substring(4);

                if (checkHave(txtTopicId) == true) {
                    Intent intent = new Intent(this, HomeActivity.class);
                    this.startActivity(intent);
                } else {
                    Toast.makeText(this, "Can not recognize device", Toast.LENGTH_LONG).show();
                    super.onActivityResult(requestCode, resultCode, data);
                }
            } else {
                Toast.makeText(this, "QR code Error", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    boolean checkHave(String txtTopicId) {
        TopicSubcription t = new TopicSubcription();
        t.setTopicID(txtTopicId);

        DownloadActivity dwn = new DownloadActivity();
        List<TopicSubcription> tpsub = dwn.getTopicSubcription();
        TopicSubcription topicSubcription = findTopicSubcription(t.getTopicID(), tpsub);
        if (topicSubcription != null) {
            return true;
        }
        return false;
    }

    public TopicSubcription findTopicSubcription(String topicID, List<TopicSubcription> topic) {
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

}
