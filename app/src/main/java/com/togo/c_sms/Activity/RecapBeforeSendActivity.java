package com.togo.c_sms.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.togo.c_sms.R;

public class RecapBeforeSendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap_before_send);
        String letter = getIntent().getStringExtra("letter");

    }
}
