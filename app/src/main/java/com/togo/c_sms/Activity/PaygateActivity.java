package com.togo.c_sms.Activity;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.togo.c_sms.Managers.MyWebViewClient;
import com.togo.c_sms.R;


public class PaygateActivity extends AppCompatActivity {

    WebView paygatepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paygate);

        String token =  getIntent().getExtras().getString("token");
        String identifier =  getIntent().getExtras().getString("montant");
        String montant =  getIntent().getExtras().getString("montant");

        paygatepage = findViewById(R.id.paygatepage);
        String description = "Recharge de compte C-sms";
        paygatepage.getSettings().setJavaScriptEnabled(true);
        paygatepage.setWebViewClient(new WebViewClient());
        paygatepage.loadUrl("https://paygateglobal.com/v1/page?token=" +token+"&amount="+montant+"&description="+description+"&identifier="+identifier+"&url=paymentactivity");
         paygatepage.setWebViewClient(new MyWebViewClient(this));


    }


}
