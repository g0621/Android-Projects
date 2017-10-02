package com.example.gyan.intouch.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gyan.intouch.R;
import com.example.gyan.intouch.activities.ChatActivity;
import com.example.gyan.intouch.activities.MainActivity;
import com.example.gyan.intouch.activities.ProfileActivity;
import com.example.gyan.intouch.models.Friends;
import com.example.gyan.intouch.viewHolders.FriendsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFrag extends Fragment {

    private RecyclerView friendRecyclerView;
    private DatabaseReference reference;
    private DatabaseReference userDatabaseRef;
    private String currentUserId;
    private View mainView;
    private FirebaseRecyclerAdapter<Friends , FriendsViewHolder> firebaseRecyclerAdapter;


    public FriendsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_friends, container, false);
        friendRecyclerView = (RecyclerView) mainView.findViewById(R.id.friendsRecyclersView);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Friends").child(currentUserId);
        reference.keepSynced(true);
        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userDatabaseRef.keepSynced(true);
        friendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return mainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(
                Friends.class,R.layout.all_user_card,FriendsViewHolder.class,reference
        ) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder viewHolder, final Friends model, int position) {

                final String key = getRef(position).getKey();
                userDatabaseRef.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        final String username = dataSnapshot.child("user_name").getValue().toString();
                        final String thumb = dataSnapshot.child("user_thumb_image").getValue().toString();
                        final String status = dataSnapshot.child("user_status").getValue().toString();
                        String date = model.getDate();
                        if (dataSnapshot.hasChild("online")){
                            String isOnline =  dataSnapshot.child("online").getValue().toString();
                            if (isOnline.equals("true")) viewHolder.setOnlineStatus();
                        }
                        viewHolder.updateUI(username,date,thumb,getContext());
                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[] = new CharSequence[]{
                                        "Visit " + username + "'s profile", "Chat with " + username
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                                        .setTitle("Options")
                                        .setItems(options, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                switch (i){
                                                    case 0:
                                                        MainActivity.goingToOtherActivity = true;
                                                        startActivity(new Intent(getContext(), ProfileActivity.class).putExtra("userName",username).putExtra("userKey",key)
                                                        .putExtra("userStatus",status).putExtra("userThumb",thumb));
                                                        break;
                                                    case 1:
                                                        if (dataSnapshot.child("online").exists()){
                                                            MainActivity.goingToOtherActivity = true;
                                                            startActivity(new Intent(getContext(), ChatActivity.class).putExtra("userKey",key).putExtra("userName",username)
                                                                    .putExtra("userThumb",thumb));
                                                        }else {
                                                            userDatabaseRef.child(key).child("online").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    MainActivity.goingToOtherActivity = true;
                                                                    startActivity(new Intent(getContext(), ChatActivity.class).putExtra("userKey",key).putExtra("userName",username)
                                                                            .putExtra("userThumb",thumb));
                                                                }
                                                            });
                                                        }
                                                        break;
                                                }
                                            }
                                        });
                                builder.show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        friendRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        firebaseRecyclerAdapter.cleanup();
    }
}
