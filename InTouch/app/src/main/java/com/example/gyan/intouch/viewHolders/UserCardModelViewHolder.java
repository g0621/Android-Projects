package com.example.gyan.intouch.viewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.gyan.intouch.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Gyan on 9/29/2017.
 */

public class UserCardModelViewHolder extends RecyclerView.ViewHolder {

    public View view;

    public UserCardModelViewHolder(View itemView) {
        super(itemView);

        view = itemView;
    }

    public void updateUI(String username, String status, final String picLink, final Context context){
        ((TextView) view.findViewById(R.id.card_user_name)).setText(username);
        ((TextView) view.findViewById(R.id.card_user_status)).setText(status);
        Picasso.with(context).load(picLink).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.defaultpic).into((CircleImageView) view.findViewById(R.id.card_user_pic), new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(context).load(picLink).placeholder(R.drawable.defaultpic).into((CircleImageView) view.findViewById(R.id.card_user_pic));
            }
        });
    }
}
