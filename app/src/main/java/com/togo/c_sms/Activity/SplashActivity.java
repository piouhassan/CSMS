package com.togo.c_sms.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.togo.c_sms.Managers.SessionManager;
import com.togo.c_sms.R;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);

        ((ImageView)findViewById(R.id.logo)).startAnimation(pulse);


        final Intent i = new Intent(this, StartActivity.class);
        Thread timer = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    Animatoo.animateSwipeLeft(SplashActivity.this);
                    startActivity(i);
              //      overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                }
            }
        };
        timer.start();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
