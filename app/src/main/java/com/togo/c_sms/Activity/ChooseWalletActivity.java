package com.togo.c_sms.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.togo.c_sms.R;

public class ChooseWalletActivity extends AppCompatActivity {
        CardView recharges;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_wallet);
        recharges = findViewById(R.id.recharges);

        recharges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseWalletActivity.this,WalletsActivity.class);
                startActivity(intent);
                Animatoo.animateSlideLeft(ChooseWalletActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(ChooseWalletActivity.this);
    }
}
