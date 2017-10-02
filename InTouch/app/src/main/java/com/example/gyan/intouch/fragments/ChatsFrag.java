package com.example.gyan.intouch.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gyan.intouch.R;
import com.example.gyan.intouch.activities.ChatActivity;
import com.example.gyan.intouch.activities.MainActivity;
import com.example.gyan.intouch.models.AllChats;
import com.example.gyan.intouch.viewHolders.AllChatsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;


public class ChatsFrag extends Fragment {

    private View mainView;
    private RecyclerView recyclerView;
    private DatabaseReference friendsReference;
    private DatabaseReference usersReference;
    private String currentUserKey;
    private FirebaseRecyclerAdapter<AllChats,AllChatsViewHolder> firebaseRecyclerAdapter;

    public ChatsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = (RecyclerView) mainView.findViewById(R.id.allchatshowrecyclewer);
        currentUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        friendsReference = FirebaseDatabase.getInstance().getReference().child("Messages").child(currentUserKey);
        usersReference =  FirebaseDatabase.getInstance().getReference().child("Users");

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);


        return mainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AllChats, AllChatsViewHolder>(
                AllChats.class,R.layout.all_user_card,AllChatsViewHolder.class,friendsReference
        ) {
            @Override
            protected void populateViewHolder(final AllChatsViewHolder viewHolder, final AllChats model, int position) {

                final String key = getRef(position).getKey();
                usersReference.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        final String username = dataSnapshot.child("user_name").getValue().toString();
                        final String thumb = dataSnapshot.child("user_thumb_image").getValue().toString();
                        final String status = dataSnapshot.child("user_status").getValue().toString();
                        if (dataSnapshot.hasChild("online")){
                            String isOnline =  dataSnapshot.child("online").getValue().toString();
                            if (isOnline.equals("true")) viewHolder.setOnlineStatus();
                        }
                        viewHolder.updateUI(username,status,thumb,getContext());
                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (dataSnapshot.child("online").exists()){
                                    MainActivity.goingToOtherActivity = true;
                                    startActivity(new Intent(getContext(), ChatActivity.class).putExtra("userKey",key).putExtra("userName",username)
                                            .putExtra("userThumb",thumb));
                                }else {
                                    usersReference.child(key).child("online").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            MainActivity.goingToOtherActivity = true;
                                            startActivity(new Intent(getContext(), ChatActivity.class).putExtra("userKey",key).putExtra("userName",username)
                                                    .putExtra("userThumb",thumb));
                                        }
                                    });
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
