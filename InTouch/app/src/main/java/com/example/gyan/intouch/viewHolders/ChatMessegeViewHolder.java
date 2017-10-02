package com.example.gyan.intouch.viewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gyan.intouch.Adapters.TimeResolver;
import com.example.gyan.intouch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Gyan on 10/1/2017.
 */

public class ChatMessegeViewHolder extends RecyclerView.ViewHolder{

    public View view;
    private DatabaseReference reference;

    public ChatMessegeViewHolder(View itemView) {
        super(itemView);

        view = itemView;
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.keepSynced(true);
    }



    public void updateUI(String message,String userId,long lastseen){

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ((TextView) view.findViewById(R.id.messegeuser_lastseen)).setText(TimeResolver.getTimeAgo(lastseen));
        TextView messageView = (TextView)view.findViewById(R.id.messegeuser_msg);
        LinearLayout mainLayout = (LinearLayout) view.findViewById(R.id.mainChatHolder);
        LinearLayout mainchatholder = (LinearLayout) view.findViewById(R.id.mainMessegeHolder);
        if (currentUserId.equals(userId)){
            mainchatholder.setBackgroundResource(R.drawable.chatmessege_bg1);
            mainLayout.setGravity(Gravity.RIGHT);

        }else {
            messageView.setBackgroundResource(R.drawable.chatmessege_bg);
            mainLayout.setGravity(Gravity.LEFT);
        }
        messageView.setText(message);
    }
}
