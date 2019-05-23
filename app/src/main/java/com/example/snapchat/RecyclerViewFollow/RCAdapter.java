package com.example.snapchat.RecyclerViewFollow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.snapchat.R;

import java.util.List;

public class RCAdapter extends RecyclerView.Adapter<RCViewHolders> {

    private List<UsersObject> usersList;
    private Context context;

    public RCAdapter (List<UsersObject> usersList, Context context){
        this.usersList = usersList;
        this.context = context;
    }
    @Override
    public RCViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_followers_item, null);
        RCViewHolders rcv = new RCViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RCViewHolders holder, int position) {
        holder.mEmail.setText(usersList.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
