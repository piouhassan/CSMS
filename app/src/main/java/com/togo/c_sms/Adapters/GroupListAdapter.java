package com.togo.c_sms.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.togo.c_sms.Activity.GroupCreateActivity;
import com.togo.c_sms.Activity.GroupDetailActivity;
import com.togo.c_sms.Activity.MessageWriteActivity;
import com.togo.c_sms.Helper.DialogHelper;
import com.togo.c_sms.Models.Group;
import com.togo.c_sms.R;

import java.util.List;


public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.MyViewHolder> {
    private Context mContext;
    private Activity activity;
    List<Group> mData;
    public GroupListAdapter(Context mContext, List<Group> mData,Activity activity) {
        this.mContext = mContext;
        this.mData = mData;
         this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View view;
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.item_group_list,viewGroup, false);

        final MyViewHolder viewHolder =  new MyViewHolder(view);
             viewHolder.grpitem.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent = new Intent(mContext, GroupDetailActivity.class);
                     intent.putExtra("gname", mData.get(viewHolder.getAdapterPosition()).getName());
                     intent.putExtra("user_hash", mData.get(viewHolder.getAdapterPosition()).getHash());
                     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     mContext.startActivity(intent);
                     Animatoo.animateSlideLeft(activity);
                 }
             });
                 viewHolder.grpitem.setOnLongClickListener(new View.OnLongClickListener() {
                 @Override
                 public boolean onLongClick(View view) {
                     DialogHelper dialogHelper = new DialogHelper(activity, mContext);
                     dialogHelper.groupOption(mData.get(viewHolder.getAdapterPosition()).getHash(),mData.get(viewHolder.getAdapterPosition()).getName(),mData.get(viewHolder.getAdapterPosition()).getUid());
                     return  false;
                 }
             });
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.gname.setText(mData.get(i).getName());
        myViewHolder.countperson.setText(mData.get(i).getNumbers());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView gname, countperson;
         private RelativeLayout grpitem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            gname = itemView.findViewById(R.id.gname);
            grpitem = itemView.findViewById(R.id.gritem);
            countperson = itemView.findViewById(R.id.countperson);
        }
    }
}
