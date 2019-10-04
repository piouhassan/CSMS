package com.togo.c_sms.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.togo.c_sms.Adapters.GroupListAdapter;
import com.togo.c_sms.Database.UserDbHelper;
import com.togo.c_sms.Helper.DialogHelper;
import com.togo.c_sms.Helper.ApiUrl;
import com.togo.c_sms.Models.Group;
import com.togo.c_sms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class GroupCreateActivity extends AppCompatActivity {
    private static final String TAG = GroupCreateActivity.class.getSimpleName();
    private UserDbHelper db;
    ImageView add_group;
    private ProgressDialog pDialog;
    private List<Group> groupList ;
    RecyclerView myrecyclerview;
    JsonArrayRequest request;
    RequestQueue requestQueue;
    DialogHelper dialogHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        add_group = findViewById(R.id.addgroup);
        dialogHelper = new DialogHelper(GroupCreateActivity.this, getApplicationContext());
        db = new UserDbHelper(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
      final   String hash = user.get("hash_key");

        myrecyclerview = (RecyclerView) findViewById(R.id.group_recycler);

        groupList = new ArrayList<>();

        add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 dialogHelper = new DialogHelper(GroupCreateActivity.this,getApplicationContext());
                 dialogHelper.addgroup();
            }
        });

        jsonRequest(hash);
    }

    public  void jsonRequest(final String hash){
        final SpotsDialog alertDialog = new SpotsDialog(GroupCreateActivity.this);
        alertDialog.show();
        alertDialog.setMessage("Veuillez Patienter...");
        request = new JsonArrayRequest(ApiUrl.URL_GROUPS+hash, new Response.Listener<JSONArray>() {
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
                        Group group = new Group();
                        group.setName(jsonObject.getString("gname"));
                        group.setHash(jsonObject.getString("c_user_hash"));
                        group.setNumbers(jsonObject.getString("number_persons"));
                        group.setUid(jsonObject.getInt("id"));
                        groupList.add(group);

                    }
                    catch (JSONException e) {
                        alertDialog.dismiss();
                        e.printStackTrace();
                    }
                }
                setuprecyclerview(groupList);
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

    private void setuprecyclerview(List<Group> groupList) {
        GroupListAdapter groupListAdapter = new GroupListAdapter(getApplicationContext(), groupList,GroupCreateActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        myrecyclerview.setLayoutManager(gridLayoutManager);
        myrecyclerview.setAdapter(groupListAdapter);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(GroupCreateActivity.this);
    }
}
