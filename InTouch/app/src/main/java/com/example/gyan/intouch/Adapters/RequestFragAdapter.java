package com.example.gyan.intouch.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gyan.intouch.R;
import com.example.gyan.intouch.viewHolders.FriendRequestCardViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Gyan on 10/2/2017.
 */

public class RequestFragAdapter extends RecyclerView.Adapter<FriendRequestCardViewHolder> {

    private List<String> allUserIds;
    private Context context;

    public RequestFragAdapter(List<String> allUserIds) {
        this.allUserIds = allUserIds;
    }

    @Override
    public FriendRequestCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_friend_requests_layout,parent,false);
        return new FriendRequestCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FriendRequestCardViewHolder holder, int position) {

        final String userKey = allUserIds.get(position);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userKey);
        databaseReference.keepSynced(true);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String name = dataSnapshot.child("user_name").getValue().toString();
                    String status = dataSnapshot.child("user_status").getValue().toString();
                    String thumb = dataSnapshot.child("user_thumb_image").getValue().toString();
                    holder.updateUI(name,status,thumb,context,userKey);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return allUserIds.size();
    }
}
