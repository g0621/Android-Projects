package com.example.gyan.intouch.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gyan.intouch.R;
import com.example.gyan.intouch.models.ChatMessege;
import com.example.gyan.intouch.viewHolders.ChatMessegeViewHolder;

import java.util.List;

/**
 * Created by Gyan on 10/1/2017.
 */

public class ChatMessegeAdapter extends RecyclerView.Adapter<ChatMessegeViewHolder> {

    private List<ChatMessege> userMesseges;


    public ChatMessegeAdapter(List<ChatMessege> userMesseges) {
        this.userMesseges = userMesseges;
    }

    @Override
    public ChatMessegeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_screen_layout,parent,false);

        return new ChatMessegeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatMessegeViewHolder holder, int position) {
        ChatMessege messege = userMesseges.get(position);
        holder.updateUI(messege.getMessage(),messege.getFrom(),messege.getTime());
    }

    @Override
    public int getItemCount() {
        return userMesseges.size();
    }
}
