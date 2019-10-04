package com.togo.c_sms.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.togo.c_sms.Database.UserDbHelper;
import com.togo.c_sms.R;

import java.util.HashMap;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class AboutActivity extends AppCompatActivity { ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateFade(AboutActivity.this);
    }
}
