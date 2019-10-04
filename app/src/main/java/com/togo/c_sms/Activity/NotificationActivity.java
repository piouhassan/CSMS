package com.togo.c_sms.Activity;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;


import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import com.google.firebase.messaging.FirebaseMessaging;
import com.togo.c_sms.R;

public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = "Notification";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        FirebaseMessaging.getInstance().subscribeToTopic("csms");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(NotificationActivity.this);
    }
}
