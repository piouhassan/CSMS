package com.togo.c_sms.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.togo.c_sms.R;

public class RecapBeforeSendActivity extends AppCompatActivity {
      TextView finaltopay,entetefinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap_before_send);
        String header = getIntent().getStringExtra("header");
        String topay = getIntent().getStringExtra("topay");
        finaltopay = findViewById(R.id.finaltopay);
        entetefinal = findViewById(R.id.entetefinal);

        finaltopay.setText(topay);
        entetefinal.setText(header);


    }
}
