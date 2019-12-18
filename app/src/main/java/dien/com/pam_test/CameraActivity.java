package dien.com.pam_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    public static String txtTopicId = null;

    //qr code scanner object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        //intializing scan object
        qrScan = new IntentIntegrator(this);
        qrScan.setPrompt("Scan QR code");
        qrScan.initiateScan();
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
            if(txtTopicId.startsWith("S/N:")) {
                txtTopicId = txtTopicId.substring(4);

                Intent intent = new Intent(this, HomeActivity.class);
                this.startActivity(intent);
            }
            else {
                Toast.makeText(this, "QR code Error", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
