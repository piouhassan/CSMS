package com.togo.c_sms.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.togo.c_sms.Database.UserDbHelper;
import com.togo.c_sms.Helper.DialogHelper;
import com.togo.c_sms.Managers.SessionManager;
import com.togo.c_sms.R;

import java.util.HashMap;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class SettingsActivity extends AppCompatActivity {
    private UserDbHelper db;
    TextView phone, thisusername;
    RelativeLayout info, logout;
    SessionManager sessionManager;
    DialogHelper dialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        PulsatorLayout pulsator = (PulsatorLayout) findViewById(R.id.pulsator);
        pulsator.start();
        sessionManager = new SessionManager(this);

        thisusername = findViewById(R.id.fullname);
        info = findViewById(R.id.info);
        logout = findViewById(R.id.logout);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
                startActivity(intent);
                Animatoo.animateSlideLeft(SettingsActivity.this);
            }
        });

        db = new UserDbHelper(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String  tel = user.get("phone");
        String name = user.get("fullname");
        thisusername.setText(name);


        dialogHelper = new DialogHelper(SettingsActivity.this, getApplicationContext());
        if (!sessionManager.isLoggedIn()) {
            dialogHelper.logoutUser();
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogHelper.LogoutVerifi();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(SettingsActivity.this);
    }
}
