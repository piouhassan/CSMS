package com.togo.c_sms.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.togo.c_sms.Adapters.ContactAdapter;
import com.togo.c_sms.Database.UserDbHelper;
import com.togo.c_sms.Helper.ApiUrl;
import com.togo.c_sms.Models.Contact;
import com.togo.c_sms.Models.Group;
import com.togo.c_sms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import static android.R.layout.simple_spinner_item;
public class ChooseReceiverActivity extends AppCompatActivity {

    private  static final String TAG = "ContactArray";
   String  message, letter;
   TextView entete,letterscount,topay;
   LinearLayout choose_receiver;
   RadioButton tocontact, togroup;
    Spinner spinner;
    private UserDbHelper db;
    LinearLayout choose_receiver_group;
     int topayed;
    private ArrayList<Group> groupArrayList;
    private ArrayList<String> names = new ArrayList<String>();
    JsonArrayRequest request;
    RequestQueue requestQueue;
    private  static final  int RESULT_PICK_CONTACT = 1;
    int finalamount;
    EditText header_content;
    RelativeLayout choosedestinataire;
    LinearLayout contactChoose;
    ArrayList<Contact> contactArrayList;
    RecyclerView.LayoutManager  choosecontactrecyclerviewLayout;
    RecyclerView.Adapter mAdapter;
    ImageView submit_header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_receiver);
        letterscount = findViewById(R.id.letterscount);
        message = getIntent().getExtras().getString("message");
        letter = getIntent().getExtras().getString("letter");
        letterscount.setText(letter);
        choose_receiver = findViewById(R.id.choose_receiver);
        choose_receiver_group = findViewById(R.id.choose_receiver_group);
        tocontact = findViewById(R.id.tocontact);
        togroup = findViewById(R.id.togroup);

        topay = findViewById(R.id.topay);

         int  amount = Integer.parseInt(letter) / 160;
         if (Integer.parseInt(letter) % 160 > 0){
            finalamount = amount + 1;
         }else{
             finalamount = amount;
         }
          topayed = finalamount * 12;

        topay.setText(new StringBuilder(String.valueOf(topayed)).append(" frcs"));


        contactArrayList = new ArrayList<>();


        spinner = (Spinner) findViewById(R.id.groupspinner);

        contactChoose = findViewById(R.id.contactChoose);
        submit_header = findViewById(R.id.submit_header);
        header_content = findViewById(R.id.header_content);
        entete = findViewById(R.id.entete);
        choosedestinataire = findViewById(R.id.choosedestinataire);

        header_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                entete.setText(header_content.getEditableText().toString());
            }
        });

        submit_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(header_content.getEditableText().toString().isEmpty()){
                    Toast.makeText(ChooseReceiverActivity.this, "Veuillez saisir l'entête de votre message", Toast.LENGTH_SHORT).show();
                }else{
                    RelativeLayout Header = findViewById(R.id.Header);
                    choosedestinataire.setVisibility(View.VISIBLE);
                    choose_receiver.setVisibility(View.VISIBLE);
                     Header.setVisibility(View.GONE);
//                    header_content.setFocusable(false);
//                    header_content.setEnabled(false);
                }
               
            }
        });



        tocontact.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                choose_receiver.setVisibility(View.GONE);
                choose_receiver_group.setVisibility(View.VISIBLE);
            }
        });

        togroup.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                choose_receiver.setVisibility(View.VISIBLE);
                choose_receiver_group.setVisibility(View.GONE);
            }
        });
        

        retrieveJSON();

        LinearLayout choose_receiver = findViewById(R.id.choose_receiver);

        choose_receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContactDialog();
            }
        });


        TextView sendmessagefinal = findViewById(R.id.sendmessagefinal);

        sendmessagefinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String header = header_content.getEditableText().toString();
                if (header.isEmpty()){
                    Toast.makeText(ChooseReceiverActivity.this, "Veuillez saisir l'entête de votre message", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(ChooseReceiverActivity.this, RecapBeforeSendActivity.class);
                    intent.putExtra("header", header);
                    intent.putExtra("letter", letter);
                    intent.putExtra("topay", String.valueOf(topay));
                    startActivity(intent);
                }
            }
        });

    }

    private void retrieveJSON() {

        db = new UserDbHelper(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final   String hash = user.get("hash_key");

        request = new JsonArrayRequest(ApiUrl.URL_GROUPS+hash, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                groupArrayList = new ArrayList<>();
                if (response.length() == 0){
                    Toast.makeText(getApplicationContext(), "Liste  vide", Toast.LENGTH_SHORT).show();
                }
                for (int i = 0 ; i < response.length(); i++){
                    try{
                        jsonObject = response.getJSONObject(i);
                        Group group = new Group();
                        group.setName(jsonObject.getString("gname"));
                        group.setHash(jsonObject.getString("c_user_hash"));
                        group.setNumbers(jsonObject.getString("number_persons"));
                        group.setUid(jsonObject.getInt("id"));
                        groupArrayList.add(group);

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < groupArrayList.size(); i++){
                    names.add(groupArrayList.get(i).getName().toString());
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ChooseReceiverActivity.this, simple_spinner_item, names);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner.setPrompt("Groupes");
                spinner.setAdapter(spinnerArrayAdapter);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    private void addtorecyclerview(String phoneNo, String name) {
        contactChoose.setVisibility(View.VISIBLE);

        RecyclerView choosecontactrecyclerview = findViewById(R.id.choosecontactrecyclerview);
        Contact contact = new Contact();
        contact.setNumero(phoneNo);
        contact.setName(name);
        contactArrayList.add(contact);
        for (int i = 0; i < contactArrayList.size();i++){
            choosecontactrecyclerview.setHasFixedSize(true);
            choosecontactrecyclerviewLayout = new LinearLayoutManager(this);
            mAdapter = new ContactAdapter(contactArrayList);
            choosecontactrecyclerview.setLayoutManager(choosecontactrecyclerviewLayout);
            choosecontactrecyclerview.setAdapter(mAdapter);
        }

        topay.setText(new StringBuilder(String.valueOf(contactArrayList.size() * topayed)).append(" frcs"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideDown(ChooseReceiverActivity.this);
    }

    private void showContactDialog(){
        AlertDialog.Builder ContactDialog = new AlertDialog.Builder(this);
        ContactDialog.setTitle("Choisir un contact");
        String[] contactdialog = {
                "Depuis le Repertoire"};
        ContactDialog.setItems(contactdialog,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                chooseContactFromContact();
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

                addtorecyclerview(phoneNo,name);

             //   dialogHelper.contactshow(gname,hash_key,phoneNo,name);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
