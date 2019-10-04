package com.togo.c_sms.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.togo.c_sms.Activity.HistoricActivity;
import com.togo.c_sms.Activity.MessageListActivity;
import com.togo.c_sms.Activity.MessageWriteActivity;
import com.togo.c_sms.Helper.ApiUrl;
import com.togo.c_sms.Models.Historic;
import com.togo.c_sms.R;

import java.util.List;


public class HistoricListAdapter extends RecyclerView.Adapter<HistoricListAdapter.MyViewHolder> {
    private Context mContext;
    List<Historic> mData;
    public HistoricListAdapter(Context mContext, List<Historic> mData) {
        this.mContext = mContext;
        this.mData = mData;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.item_message,viewGroup, false);

        final MyViewHolder viewHolder =  new MyViewHolder(view);
        viewHolder.historic_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HistoricActivity.class);
                intent.putExtra("type", mData.get(viewHolder.getAdapterPosition()).getType());
                intent.putExtra("message", mData.get(viewHolder.getAdapterPosition()).getMessageContent());
                intent.putExtra("date", mData.get(viewHolder.getAdapterPosition()).getSenddate());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                Animatoo.animateSlideUp(mContext);
            }
        });

        viewHolder.historic_single.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                viewHolder.delete_historic_single.setVisibility(View.VISIBLE);
                final LayoutInflater inflater = LayoutInflater.from(mContext);

                return true;
            }
        });

        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.historic_send_nom.setText(mData.get(i).getContactName());
        myViewHolder.historic_few_content.setText(mData.get(i).getMessageContent().substring(0,50)+"...");
        Picasso.get().load(ApiUrl.URL_IMAGE+mData.get(i).getType()).placeholder(R.drawable.user).into(myViewHolder.historic_pic);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView historic_send_nom,historic_few_content;
        private MaterialRippleLayout historic_single;
        private CircularImageView historic_pic;
          private ImageView delete_historic_single;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            historic_send_nom = itemView.findViewById(R.id.historic_send_name);
            historic_single = itemView.findViewById(R.id.historic_single);
            historic_pic = itemView.findViewById(R.id.historic_pic);
            historic_few_content = itemView.findViewById(R.id.historic_few_content);
            delete_historic_single = itemView.findViewById(R.id.delete_historic_single);
        }
    }
}
