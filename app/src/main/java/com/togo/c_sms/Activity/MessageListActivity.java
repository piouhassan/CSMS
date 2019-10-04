package com.togo.c_sms.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.togo.c_sms.Adapters.HistoricListAdapter;
import com.togo.c_sms.Database.UserDbHelper;
import com.togo.c_sms.Helper.ApiUrl;
import com.togo.c_sms.Models.Historic;
import com.togo.c_sms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MessageListActivity extends AppCompatActivity {
    private static final String TAG = GroupCreateActivity.class.getSimpleName();
    FloatingActionButton floatingActionButton;
    private List<Historic> historicList ;
    RecyclerView myrecyclerview;
    JsonArrayRequest request;
    RequestQueue requestQueue;
    UserDbHelper db;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        db = new UserDbHelper(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final   String hash = user.get("hash_key");


        myrecyclerview = findViewById(R.id.message_historic_recycler);

        historicList = new ArrayList<>();
        floatingActionButton = findViewById(R.id.tomessagewrite);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageListActivity.this,MessageWriteActivity.class);
                startActivity(intent);
                Animatoo.animateSlideUp(MessageListActivity.this);
            }
        });

        jsonRequest(hash);
    }


    public   void jsonRequest(final String hash){
        final SpotsDialog alertDialog = new SpotsDialog(MessageListActivity.this);
        alertDialog.show();
        alertDialog.setMessage("Veuillez Patienter...");
        request = new JsonArrayRequest(ApiUrl.URL_HISTORIC+hash, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                if (response.length() == 0){
                    alertDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Liste  vide", Toast.LENGTH_SHORT).show();
                }
                for (int i = 0 ; i < response.length(); i++){
                   alertDialog.dismiss();
                    try{
                        jsonObject = response.getJSONObject(i);
                        Historic historic = new Historic();
                        historic.setType(jsonObject.getString("type"));
                            historic.setContactGroup(jsonObject.getString("group_name"));
                        historic.setUid(jsonObject.getInt("id"));
                        historic.setContactName(jsonObject.getString("contact_name"));
                        historic.setContactNumero(jsonObject.getString("contact_number"));
                        historic.setMessageContent(jsonObject.getString("message_content"));
                        historic.setSenddate(jsonObject.getString("created_at"));
                        historicList.add(historic);
                    }
                    catch (JSONException e) {
                        alertDialog.dismiss();
                        e.printStackTrace();
                    }
                }
                setuprecyclerview(historicList);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    private void setuprecyclerview(List<Historic> historicList) {
        HistoricListAdapter historicListAdapter = new HistoricListAdapter(this, historicList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        myrecyclerview.setLayoutManager(linearLayoutManager);
        myrecyclerview.setAdapter(historicListAdapter);

    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(MessageListActivity.this);
    }
}
