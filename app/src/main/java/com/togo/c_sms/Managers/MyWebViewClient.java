package com.togo.c_sms.Managers;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.togo.c_sms.Activity.PayConfirmationActivity;

public class MyWebViewClient extends WebViewClient {

    private Context context;

    public MyWebViewClient(Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if(url.equals("/online/paymentactivity")){
            Intent i = new Intent(context, PayConfirmationActivity.class);
            context.startActivity(i);
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }
}
