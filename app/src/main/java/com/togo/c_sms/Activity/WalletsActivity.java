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
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.togo.c_sms.Database.UserDbHelper;
import com.togo.c_sms.Helper.ApiUrl;
import com.togo.c_sms.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import dmax.dialog.SpotsDialog;

public class WalletsActivity extends AppCompatActivity {

    TextView wallet_next,solderestant,messagerestant;
    EditText  wallet_current;
    private UserDbHelper db;
    private ProgressDialog pDialog;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallets);

        wallet_next = findViewById(R.id.wallet_next);
        wallet_current = findViewById(R.id.wallet_current);



        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Chargement en cours");
        token = "12cf8234-5956-46cf-941d-27780cb0db0d";

        UserDbHelper userDbHelper = new UserDbHelper(getApplicationContext());
        HashMap<String, String> user = userDbHelper.getUserDetails();
        final   String hash = user.get("hash_key");

        jsonrequest(hash);

        wallet_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String montant  = wallet_current.getEditableText().toString().trim();
                if (montant.isEmpty()){
                    wallet_current.setError("Veuillez definir d'abord le prix");
                }
                if (Integer.valueOf(montant) < 1){
                    wallet_current.setError("Veuillez  saisir un montant superieur ou égal à 200");
                }else{
                    db = new UserDbHelper(getApplicationContext());
                    HashMap<String, String> user = db.getUserDetails();
                    String  hash_key = user.get("hash_key");
                    Random r = new Random();
                    int identifier = r.nextInt(1000000);
                    paymenJsonSend(montant,identifier,hash_key);
                }

            }
        });
    }

    private void paymenJsonSend(final String montant, final int identifier, final String hash_key) {
        final SpotsDialog alertDialog = new SpotsDialog(WalletsActivity.this);
        alertDialog.show();
        alertDialog.setMessage("Veuillez Patienter...");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.URL_PAYMENT_FIRST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    boolean error = json.getBoolean("error");
                    if(!error)
                    {
                            Random r = new Random();
                             token = "12cf8234-5956-46cf-941d-27780cb0db0d";
                             int identifier = r.nextInt(1000000);

                        Intent intent = new Intent(WalletsActivity.this,PaygateActivity.class);
                        intent.putExtra("montant", montant );
                        intent.putExtra("token", token );
                        intent.putExtra("identifier", identifier );
                        startActivity(intent);
                        alertDialog.dismiss();
                    }else{
                        String message = json.getString("message");
                        Toast.makeText(WalletsActivity.this, message, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }

                } catch (JSONException e) {
                    alertDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(WalletsActivity.this, "Error1 "+ e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        alertDialog.dismiss();
                        Toast.makeText(WalletsActivity.this, "Error2 "+error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }

        ){
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("hash_key", hash_key);
                map.put("montant", montant);
                map.put("identifier", String.valueOf(identifier));
                return  map;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public   void jsonrequest(final String hash) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ApiUrl.URL_GET_CREDIT+hash;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                         int sms =Integer.parseInt(response)  / 12;
                        solderestant = findViewById(R.id.solderestant);
                        messagerestant = findViewById(R.id.messagerestant);
                        solderestant.setText(response);
                        messagerestant.setText(String.valueOf(sms));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(WalletsActivity.this);
    }

}
