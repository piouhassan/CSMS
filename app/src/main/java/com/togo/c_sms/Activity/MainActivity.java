package com.togo.c_sms.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.togo.c_sms.Database.UserDbHelper;
import com.togo.c_sms.Helper.DialogHelper;
import com.togo.c_sms.Helper.ApiUrl;
import com.togo.c_sms.R;


import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
     CardView messagelist, groupcreate, wallet,parametres;
     ImageView notifications, user;
    RequestQueue requestQueue;
    TextView solde;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final String PREFS_NAME = "termecondition";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            launchdialog();
            settings.edit().putBoolean("my_first_time", false).commit();
        }


        notifications = findViewById(R.id.notifications);
        user = findViewById(R.id.user);


        messagelist = findViewById(R.id.messageswriter);
        groupcreate = findViewById(R.id.grpecreate);
        wallet = findViewById(R.id.wallet);
        parametres = findViewById(R.id.parametres);

        solde = findViewById(R.id.solde);


        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(MainActivity.this,NotificationActivity.class);
                startActivity(intent);
                Animatoo.animateSlideLeft(MainActivity.this);
            }
        });


        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                Animatoo.animateSlideLeft(MainActivity.this);
            }
        });



         messagelist.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                Intent intent  = new Intent(MainActivity.this,MessageListActivity.class);
                startActivity(intent);
                 Animatoo.animateSlideLeft(MainActivity.this);
             }
         });

        groupcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(MainActivity.this,GroupCreateActivity.class);
                startActivity(inten);
                Animatoo.animateSlideLeft(MainActivity.this);
            }
        });

        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte  = new Intent(MainActivity.this,ChooseWalletActivity.class);
                startActivity(inte);
                Animatoo.animateSlideLeft(MainActivity.this);
            }
        });

        parametres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intents  = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intents);
                Animatoo.animateSlideLeft(MainActivity.this);
            }
        });


        // Creates the Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        UserDbHelper userDbHelper = new UserDbHelper(getApplicationContext());
        HashMap<String, String> user = userDbHelper.getUserDetails();
        final   String hash = user.get("hash_key");

          jsonrequest(hash);


    }


    private void launchdialog() {
        DialogHelper dialogHelper = new DialogHelper(MainActivity.this,getApplicationContext());
          dialogHelper.firsttimeTerms();
    }



    public   void jsonrequest(final String hash) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ApiUrl.URL_GET_CREDIT+hash;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        solde.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
    }
    public void onBackPressed() {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Voulez vous quitter C-SMS ???").setCancelable(false).setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.super.onBackPressed();
                }
            })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
    }
}
