package com.togo.c_sms.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.togo.c_sms.Activity.GroupCreateActivity;
import com.togo.c_sms.Activity.GroupDetailActivity;
import com.togo.c_sms.Activity.MainActivity;
import com.togo.c_sms.Activity.StartActivity;
import com.togo.c_sms.Activity.TermNotAcceptedActivity;
import com.togo.c_sms.Database.ContactDbHelper;
import com.togo.c_sms.Database.UserDbHelper;
import com.togo.c_sms.Managers.SessionManager;
import com.togo.c_sms.Models.Group;
import com.togo.c_sms.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class DialogHelper {
    Activity activity;
    Context mContext;
    private UserDbHelper db;
    SessionManager sessionManager;
    private ProgressDialog pDialog;
    private ContactDbHelper contactDbHelper;
    public DialogHelper(Activity activity, Context mContext) {
        this.activity = activity;
        this.mContext = mContext;
        db = new UserDbHelper(activity);
        contactDbHelper = new ContactDbHelper(activity);
        sessionManager = new SessionManager(this.mContext);
        db = new UserDbHelper(mContext);

    }

    public void LogoutVerifi(){
        final Dialog dialoglogout = new Dialog(activity);
        dialoglogout.setContentView(R.layout.dialog_logout_warning);
        dialoglogout.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialoglogout.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((TextView) dialoglogout.findViewById(R.id.logoutConf)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             logoutUser();
            }
        });

        ((TextView) dialoglogout.findViewById(R.id.logoutAnn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoglogout.dismiss();

            }
        });

        dialoglogout.show();
        dialoglogout.getWindow().setAttributes(lp);
    }

    public void AccountVerify(){
        final Dialog dialogaccount = new Dialog(activity);
        dialogaccount.setContentView(R.layout.dialog_verify_account);
        dialogaccount.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogaccount.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((Button) dialogaccount.findViewById(R.id.conf)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogaccount.dismiss();
                Intent inten = new Intent(mContext, MainActivity.class);
                inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(inten);
                activity.finish();
            }
        });

        dialogaccount.show();
        dialogaccount.getWindow().setAttributes(lp);
    }

    public void internetWarning(){
        final Dialog dialog = new Dialog(activity);
      dialog.setContentView(R.layout.dialog_no_internet);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((Button) dialog.findViewById(R.id.retry)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }



    public void logoutUser() {
        sessionManager.setLogin(false);
        db.deleteUsers();
        Intent intent = new Intent(activity, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        Toast.makeText(activity, "Deconnecter avec succes", Toast.LENGTH_SHORT).show();
    }

    public void addgroup(){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_group_ad);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        db = new UserDbHelper(mContext);
        HashMap<String, String> user = db.getUserDetails();
        final   String hash_key = user.get("hash_key");


         final EditText groupe = dialog.findViewById(R.id.edt_grpe);
         final ProgressBar bar = dialog.findViewById(R.id.progress);
         final TextView loadingtext = dialog.findViewById(R.id.loadingtext);
       final Button  addgrptoserver =  dialog.findViewById(R.id.addgrptoserver);
        addgrptoserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String groupename = groupe.getEditableText().toString().trim();
              if (groupename.isEmpty()){
                  groupe.setError("Veuillez remplir le champ");
              }else {
                  addgroup(groupename, hash_key,bar,addgrptoserver,groupe,loadingtext,dialog);
              }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private void addgroup(final String groupename, final String hash_key, ProgressBar bar, Button addgrptoserver, EditText groupe, TextView loadingtext, final Dialog dialog) {
        loadingtext.setVisibility(View.VISIBLE);
        addgrptoserver.setVisibility(View.GONE);
        bar.setVisibility(View.VISIBLE);
        groupe.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.URL_GROUPS_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    boolean error = json.getBoolean("error");
                    String message = json.getString("message");
                    if(!error){
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(activity, GroupCreateActivity.class);
                        activity.finish();
                        activity.overridePendingTransition(0, 0);
                        activity.startActivity(i);
                        activity.overridePendingTransition(0, 0);
                        dialog.dismiss();
                    }else{
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity, "Error2 "+error.toString(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }

        ){
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("gname", groupename);
                map.put("hash_key", hash_key);
                return  map;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);


    }

    public void addpersongroup(final String gname){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.item_add_person_to_group);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        db = new UserDbHelper(mContext);
        HashMap<String, String> user = db.getUserDetails();
        final   String hash_key = user.get("hash_key");


        final EditText nom = dialog.findViewById(R.id.edt_name);
        final EditText numero = dialog.findViewById(R.id.numero);
        final ProgressBar bar = dialog.findViewById(R.id.progress);
        final TextView loadingtext = dialog.findViewById(R.id.loadingtext);
        final Button  addgrptoserver =  dialog.findViewById(R.id.addgrptoserver);
        addgrptoserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = nom.getEditableText().toString().trim();
                String Num = numero.getEditableText().toString().trim();
                if (Name.isEmpty()){
                    nom.setError("Veuillez remplir le champ");
                }else if(Num.isEmpty()){
                    numero.setError("Veuillez remplir le champ");
                }else{
                    loadingtext.setVisibility(View.VISIBLE);
                    addgrptoserver.setVisibility(View.GONE);
                    nom.setVisibility(View.GONE);
                    numero.setVisibility(View.GONE);
                    bar.setVisibility(View.VISIBLE);
                    SavenumeroToDatabaseafterserver(gname,hash_key,Num,Name,dialog);
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void contactshow(final String gname, final String hash_key,final String phone, final String nom){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_contact_single);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView  numbername =  dialog.findViewById(R.id.contactnom);
        TextView  numbernumero =  dialog.findViewById(R.id.numbernumero);
        numbername.setText(nom);
        numbernumero.setText(phone);

        dialog.findViewById(R.id.submitcontact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SavenumeroToDatabaseafterserver(gname,hash_key,phone,nom,dialog);
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void SavenumeroToDatabaseafterserver(final String gname, final String hash_key, final String phone, final String nom,final  Dialog dialog) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.URL_CONTACT_TO_GROUP_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    boolean error = json.getBoolean("error");
                    String message = json.getString("message");
                    if(!error){
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        contactDbHelper.addcontacttogroup(nom,phone,gname,hash_key);
                        Intent i = new Intent(activity, GroupDetailActivity.class);
                        i.putExtra("user_hash", hash_key);
                        i.putExtra("gname", gname);
                        activity.finish();
                        activity.overridePendingTransition(0, 0);
                        activity.startActivity(i);
                        activity.overridePendingTransition(0, 0);
                        dialog.dismiss();
                    }else{
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity, "Error2 "+error.toString(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }

        ){
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("gname", gname);
                map.put("cname", nom);
                map.put("cnumber", phone);
                map.put("hash_key", hash_key);
                return  map;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }



    public void groupOption(final String hash, final String gname,final int uid){
        final Dialog dialoggroupoption = new Dialog(activity);
        dialoggroupoption.setContentView(R.layout.dialog_menu_option_group);
        dialoggroupoption.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialoggroupoption.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialoggroupoption.findViewById(R.id.grp_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoggroupoption.dismiss();
                addgroup();
            }
        });

        dialoggroupoption.findViewById(R.id.grp_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 dialoggroupoption.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Suppression");
                builder.setMessage("Etes vous sure de vouloir supprimer ce groupe ? \n  \n tous les contacts seront perdu")

                          .setCancelable(false).setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               deletegroup(hash,gname,uid);
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
        });

        dialoggroupoption.show();
        dialoggroupoption.getWindow().setAttributes(lp);
    }


    public  void deletegroup(final String hash, final String gname, final int uid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.URL_GROUPS_DELETE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    boolean error = json.getBoolean("error");
                    String message = json.getString("message");
                    if(!error){
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(activity, GroupCreateActivity.class);
                        activity.finish();
                        activity.overridePendingTransition(0, 0);
                        activity.startActivity(i);
                        activity.overridePendingTransition(0, 0);
                    }else{
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, "Error2 "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

        ){
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("gname", gname);
                map.put("hash_key", hash);
                map.put("uid", String.valueOf(uid));
                return  map;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }


    public void firsttimeTerms(){
        final Dialog dialogterms = new Dialog(activity);
        dialogterms.setContentView(R.layout.dialog_term_of_services);
        dialogterms.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogterms.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        
        dialogterms.findViewById(R.id.bt_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Bienvenue sur C-SMS", Toast.LENGTH_SHORT).show();
                dialogterms.dismiss();
            }
        });

        dialogterms.findViewById(R.id.bt_decline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TermNotAcceptedActivity.class);
                mContext.startActivity(intent);
                activity.finish();
                dialogterms.dismiss();
            }
        });

        dialogterms.show();
        dialogterms.getWindow().setAttributes(lp);
    }






}
