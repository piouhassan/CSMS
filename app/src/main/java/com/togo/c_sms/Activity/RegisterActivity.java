package com.togo.c_sms.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.togo.c_sms.Database.UserDbHelper;
import com.togo.c_sms.Helper.DialogHelper;
import com.togo.c_sms.Helper.ApiUrl;
import com.togo.c_sms.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText fullname;
    EditText telephone;
    EditText adress;
    EditText password;
     TextView reg_button;
    private UserDbHelper db;
    private ProgressDialog pDialog;
    DialogHelper dialogHelper;
    String firebase_notification_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebase_notification_key  =  FirebaseInstanceId.getInstance().getToken();
      fullname = findViewById(R.id.reg_fullname);
      username = findViewById(R.id.reg_usernme);
      telephone = findViewById(R.id.reg_telephone);
      adress = findViewById(R.id.reg_address);
      password = findViewById(R.id.reg_pass);
      reg_button = findViewById(R.id.reg_button);

        dialogHelper = new DialogHelper(this, getApplicationContext());
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Creation du compte en cours");

        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String reg_username = username.getEditableText().toString().trim();
                String reg_fullname = fullname.getEditableText().toString().trim();
                String reg_password = password.getEditableText().toString().trim();
                String reg_address = adress.getEditableText().toString().trim();
                String reg_telephone= telephone.getEditableText().toString().trim();

                if(reg_username.isEmpty()){
                    username.setError("Veuillez saisir votre Identifiant");
                }

                else if(reg_fullname.isEmpty()){
                    fullname.setError("Veuillez saisir votre mot de passe");
                }
                else if(reg_telephone.isEmpty()){
                    telephone.setError("Veuillez saisir votre mot de passe");
                }
                else if(reg_address.isEmpty()){
                    adress.setError("Veuillez saisir votre mot de passe");
                }
                else if(reg_password.isEmpty()){
                    password.setError("Veuillez saisir votre mot de passe");
                }
                else{
                   register(reg_fullname,reg_username,reg_telephone,reg_address,reg_password,firebase_notification_key);
                }
            }
        });


    }

    private void register(final String fullname, final String username,final String telephone,final String address, final String password,final String firebase_notification_key ) {
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    boolean error = json.getBoolean("error");
                    if(!error){
                        Toast.makeText(RegisterActivity.this, "Compte creer avec succes", Toast.LENGTH_SHORT).show();
                         Intent  intent = new Intent(RegisterActivity.this,LoginActivity.class);
                         startActivity(intent);
                         finish();
                    }else{
                        String message = json.getString("message");
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }

                } catch (JSONException e) {
                    hideDialog();
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Erreur de connexion veuillez Réessayer", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Toast.makeText(RegisterActivity.this, "Erreur de connexion veuillez Réessayer", Toast.LENGTH_SHORT).show();

                    }
                }

        ){
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("fullname", fullname);
                map.put("username", username);
                map.put("telephone", telephone);
                map.put("address", address);
                map.put("password", password);
                map.put("firebase_token", firebase_notification_key);
                return  map;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
