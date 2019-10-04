package com.togo.c_sms.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.togo.c_sms.Models.Contact;
import com.togo.c_sms.R;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    ArrayList<Contact> contactArrayList;

    public ContactAdapter(ArrayList<Contact> contactArrayList) {
        this.contactArrayList = contactArrayList;
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_choose,parent,false);
        final ViewHolder viewHolder =  new  ViewHolder(view);
        viewHolder.removefromsender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    removeAt(viewHolder.getAdapterPosition());
                Log.d("TANG0", String.valueOf(viewHolder.getAdapterPosition()));
            }
        });

      return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
           holder.choosecontact_name.setText(contactArrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return  contactArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private   TextView  choosecontact_name;
        private ImageView removefromsender;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            choosecontact_name = itemView.findViewById(R.id.choosecontact_name);
            removefromsender = itemView.findViewById(R.id.removefromsender);

        }
    }


    public void removeAt(int position) {
        contactArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, contactArrayList.size());
    }
}
