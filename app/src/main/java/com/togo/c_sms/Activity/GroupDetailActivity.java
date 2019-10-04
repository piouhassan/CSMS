package com.togo.c_sms.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.togo.c_sms.Adapters.GroupListAdapter;
import com.togo.c_sms.Adapters.SingleContactAdapter;
import com.togo.c_sms.Database.ContactDbHelper;
import com.togo.c_sms.Helper.ApiUrl;
import com.togo.c_sms.Helper.DialogHelper;
import com.togo.c_sms.Models.Contact;
import com.togo.c_sms.Models.Group;
import com.togo.c_sms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class GroupDetailActivity extends AppCompatActivity {
    DialogHelper dialogHelper;
     FloatingActionButton addperson;
    String hash_key, gname;
     private  static final  int RESULT_PICK_CONTACT = 1;
      List<Contact> contactList;
      ArrayAdapter adapter;
    RecyclerView  contactlist;
    JsonArrayRequest request;
    RequestQueue requestQueue;
    TextView grpct;
    private static final String TAG = GroupDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        addperson = findViewById(R.id.addperson);
        contactlist = findViewById(R.id.contactlist);
        grpct = findViewById(R.id.grpct);



        dialogHelper = new DialogHelper(GroupDetailActivity.this,getApplicationContext());
        addperson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showContactDialog();
            }
        });

        contactList = new ArrayList<>();

        hash_key = getIntent().getExtras().getString("user_hash");
        gname = getIntent().getExtras().getString("gname");
        grpct.setText(gname);
        viewData();
    }

    public void viewData() {
        final SpotsDialog alertDialog = new SpotsDialog(GroupDetailActivity.this);
        alertDialog.show();
        alertDialog.setMessage("Veuillez Patienter...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.URL_GROUP_DETAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject json = new JSONObject(response);
                    boolean error = json.getBoolean("error");
                    if(!error)
                    {
                        JSONArray jsonArray = json.getJSONArray("contacts");
                          if (jsonArray.length() == 0){
                              alertDialog.dismiss();
                              Toast.makeText(GroupDetailActivity.this, "Liste vide", Toast.LENGTH_SHORT).show();
                          }else{
                              for (int i = 0 ; i < jsonArray.length(); i++){
                                  JSONObject jsonObject = jsonArray.getJSONObject(i);
                                  Contact contact = new Contact();
                                  contact.setGname(jsonObject.getString("gname"));
                                  contact.setNumero(jsonObject.getString("contact_numero"));
                                  contact.setName(jsonObject.getString("contact_name"));
                                  contactList.add(contact);
                                  alertDialog.dismiss();
                              }
                          }

                    }else{
                        alertDialog.dismiss();
                        String message = json.getString("message");
                        Toast.makeText(GroupDetailActivity.this, message, Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    alertDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(GroupDetailActivity.this, "Error1 "+ e.toString(), Toast.LENGTH_SHORT).show();
                }
                setuprecyclerview(contactList);
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        alertDialog.dismiss();
                        Toast.makeText(GroupDetailActivity.this, "Error2 "+error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }

        ){
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("hash_key", hash_key);
                map.put("gname", gname);
                return  map;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void setuprecyclerview(List<Contact> listItem) {
        SingleContactAdapter singleContactAdapter = new SingleContactAdapter(getApplicationContext(), listItem,GroupDetailActivity.this,gname);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        contactlist.setLayoutManager(linearLayoutManager);
        contactlist.setAdapter(singleContactAdapter);

    }

    private void showContactDialog(){
        AlertDialog.Builder ContactDialog = new AlertDialog.Builder(this);
        ContactDialog.setTitle("Ajouter un contact");
        String[] contactdialog = {
                "Depuis le Repertoire",
                "Ajouter manuellement" };
        ContactDialog.setItems(contactdialog,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                chooseContactFromContact();
                                break;
                            case 1:
                               writenewcontact();
                                break;
                        }
                    }
                });
        ContactDialog.show();
    }

    private void chooseContactFromContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, RESULT_PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      if (resultCode == RESULT_OK){
          switch (requestCode){
              case RESULT_PICK_CONTACT:
                  contactPicked(data);
                  break;
          }
      }else{
          Toast.makeText(this, "Recuperation du contact annulé", Toast.LENGTH_SHORT).show();
      }
    }


    private void contactPicked(Intent data) {
        try{
          String phoneNo = null;
           Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
             cursor.moveToFirst();
           int phonenumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            phoneNo = cursor.getString(phonenumber);
            if (phoneNo.length() > 8 ||  phoneNo.length() < 8 ){
                Toast.makeText(this,  "Longueur du numero invalide", Toast.LENGTH_SHORT).show();
            }else {
                dialogHelper.contactshow(gname,hash_key,phoneNo,name);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void writenewcontact() {
        dialogHelper.addpersongroup(gname);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(GroupDetailActivity.this);
    }
}
