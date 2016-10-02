package com.myhackathonproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.samples.vision.ocrreader.OcrCaptureActivity;

public class TextRecognitionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_recognition);

        Button btn_scan = (Button) findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(TextRecognitionActivity.this, OcrCaptureActivity.class);
                TextRecognitionActivity.this.startActivity(mainIntent);
                TextRecognitionActivity.this.finish();
            }
        });
    }
}
