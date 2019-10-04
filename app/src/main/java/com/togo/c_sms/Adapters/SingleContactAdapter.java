package com.togo.c_sms.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.togo.c_sms.Activity.GroupCreateActivity;
import com.togo.c_sms.Activity.GroupDetailActivity;
import com.togo.c_sms.Database.UserDbHelper;
import com.togo.c_sms.Helper.ApiUrl;
import com.togo.c_sms.Models.Contact;
import com.togo.c_sms.Models.Group;
import com.togo.c_sms.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class SingleContactAdapter extends RecyclerView.Adapter<SingleContactAdapter.MyViewHolder> {

    private Context mContext;
    List<Contact> mData;
    Activity activity;
    String gname;
    public SingleContactAdapter(Context mContext, List<Contact> mData, Activity activity,String gname) {
        this.mContext = mContext;
        this.mData = mData;
        this.activity = activity;
        this.gname = gname;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.item_grp_person,viewGroup, false);
        final MyViewHolder viewHolder =  new MyViewHolder(view);
        viewHolder.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Suppression");
                builder.setMessage("Etes vous sure de vouloir supprimer ce Contact ? \n ")

                        .setCancelable(false).setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserDbHelper db = new UserDbHelper(mContext.getApplicationContext());
                        db = new UserDbHelper(mContext);
                        HashMap<String, String> user = db.getUserDetails();
                        final   String hash_key = user.get("hash_key");
                        deletecontatct(hash_key, gname,mData.get(viewHolder.getAdapterPosition()).getName(),mData.get(viewHolder.getAdapterPosition()).getNumero());
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
        return  viewHolder;
    }

    private void deletecontatct( final String hash_key, final String gname, final String name, final String numero) {
        final SpotsDialog alertDialog = new SpotsDialog(activity);
        alertDialog.show();
        alertDialog.setMessage("Suppression en cours...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.URL_CONTACT_DELETE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    boolean error = json.getBoolean("error");
                    String message = json.getString("message");
                    if(!error){
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(activity, GroupDetailActivity.class);
                        i.putExtra("user_hash",hash_key);
                        activity.finish();
                        activity.overridePendingTransition(0, 0);
                        activity.startActivity(i);
                        activity.overridePendingTransition(0, 0);
                        alertDialog.dismiss();
                    }else{
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
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
                        alertDialog.dismiss();
                    }
                }

        ){
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("gname", gname);
                map.put("hash_key", hash_key);
                map.put("number", numero);
                map.put("cname",  name);
                return  map;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.contactsinglename.setText(mData.get(i).getName());
        myViewHolder.contactsinglenumero.setText(mData.get(i).getNumero());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder {

          TextView contactsinglename, contactsinglenumero;
           LinearLayout grpsinglecontact;
           ImageView trash;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            contactsinglename = itemView.findViewById(R.id.contactsinglename);
            contactsinglenumero = itemView.findViewById(R.id.contactsinglenumero);
            grpsinglecontact = itemView.findViewById(R.id.grpsinglecontact);
            trash = itemView.findViewById(R.id.delcontact);
        }
    }
}
