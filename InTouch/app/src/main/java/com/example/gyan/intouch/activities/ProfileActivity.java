package com.example.gyan.intouch.activities;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gyan.intouch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView circleImageView;
    private TextView profileShowUsername, profileShowStatus;
    private String username, status, picLink, tappedUserKey, currentUserKey, CURRENT_STATE;   //tapped is the receiver current is sender
    private Button acceptBtn, declineBtn;
    private DatabaseReference friendRequestRefrence, friendReference , notificationReference;

    private void tost(View view, String s) {
        Snackbar.make(view, s, Snackbar.LENGTH_LONG).show();
    }

    public void declineFriendRequest(View view){
        disConnectWith(view,2);
    }

    private void connectWith(final View view) {
        friendRequestRefrence.child(currentUserKey).child(tappedUserKey).child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            friendRequestRefrence.child(tappedUserKey).child(currentUserKey).child("request_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        HashMap<String ,String> notificationData = new HashMap<>();
                                        notificationData.put("from",currentUserKey);
                                        notificationData.put("type","request");
                                        notificationReference.child(tappedUserKey).push().setValue(notificationData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    acceptBtn.setEnabled(true);
                                                    CURRENT_STATE = "request_sent";
                                                    acceptBtn.setText("Cancel Friend Request");
                                                    tost(view, "Friend request sent 🎆 🎉 ✨");
                                                }else {
                                                    tost(view,"Unable to send request ☹, try again");
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
    }

    public void disConnectWith(final View view, final int t){
        friendRequestRefrence.child(currentUserKey).child(tappedUserKey).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            friendRequestRefrence.child(tappedUserKey).child(currentUserKey).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                if (t == 1){
                                                    acceptBtn.setEnabled(true);
                                                    CURRENT_STATE = "not_friends";
                                                    acceptBtn.setText("Send Friend Request");
                                                    tost(view,"Request Canceled 😐");
                                                }
                                                if (t == 2){
                                                    declineBtn.setEnabled(false);
                                                    declineBtn.setVisibility(View.INVISIBLE);
                                                    CURRENT_STATE = "not_friends";
                                                    acceptBtn.setText("Send Friend Request");
                                                    tost(view,username + "'s friend request declined 💔");
                                                }
                                                if (t == 0){
                                                    acceptBtn.setEnabled(true);
                                                    CURRENT_STATE = "friends";
                                                    acceptBtn.setText("Unfriend");
                                                    declineBtn.setVisibility(View.INVISIBLE);
                                                    declineBtn.setEnabled(false);
                                                    tost(view,"Now Friend with " + username + " 😊");
                                                }
                                            }else {
                                                tost(view,"Task unSuccessful please try again ☹");
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void acceptRequest(final View view){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-YYYY");
        final String date = currentdate.format(calendar.getTime());
        friendReference.child(currentUserKey).child(tappedUserKey).child("date").setValue(date)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        friendReference.child(tappedUserKey).child(currentUserKey).child("date").setValue(date)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        disConnectWith(view,0);
                                    }
                                });
                    }
                });
    }

    public void unFriendWith(final View view){
        friendReference.child(currentUserKey).child(tappedUserKey).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            friendReference.child(tappedUserKey).child(currentUserKey).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            acceptBtn.setEnabled(true);
                                            CURRENT_STATE = "not_friends";
                                            acceptBtn.setText("Send Friend Request");
                                            tost(view,"Removed " + username + " from friends list");
                                        }
                                    });
                        }
                    }
                });
    }

    public void sendFriendRequest(View view) {
        acceptBtn.setEnabled(false);
        if (CURRENT_STATE.equals("not_friends")) {
            connectWith(view);
        }
        if (CURRENT_STATE.equals("request_sent")) {
            disConnectWith(view,1);
        }
        if (CURRENT_STATE.equals("request_received")){
            acceptRequest(view);
        }
        if (CURRENT_STATE.equals("friends")){
            unFriendWith(view);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setSupportActionBar((Toolbar) findViewById(R.id.profilePage_appbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        circleImageView = (CircleImageView) findViewById(R.id.profileShowPic);
        profileShowUsername = (TextView) findViewById(R.id.profileUserName);
        profileShowStatus = (TextView) findViewById(R.id.profileUserStatus);
        acceptBtn = (Button) findViewById(R.id.sendrequestBtn);
        declineBtn = (Button) findViewById(R.id.declinereqBtn);

        CURRENT_STATE = "not_friends";

        declineBtn.setVisibility(View.INVISIBLE);
        declineBtn.setEnabled(false);

        Intent intent = getIntent();
        username = intent.getStringExtra("userName");
        getSupportActionBar().setTitle(username + "'s Profile");
        status = intent.getStringExtra("userStatus");
        picLink = intent.getStringExtra("userThumb");
        tappedUserKey = intent.getStringExtra("userKey");
        currentUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();



        if (tappedUserKey.equals(currentUserKey)){
            acceptBtn.setVisibility(View.INVISIBLE);
            declineBtn.setVisibility(View.INVISIBLE);
        }

        friendRequestRefrence = FirebaseDatabase.getInstance().getReference().child("Friend_Requests");
        friendRequestRefrence.keepSynced(true);
        friendReference = FirebaseDatabase.getInstance().getReference().child("Friends");
        friendRequestRefrence.keepSynced(true);
        notificationReference = FirebaseDatabase.getInstance().getReference().child("Notifications");
        notificationReference.keepSynced(true);

        friendRequestRefrence.child(currentUserKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(tappedUserKey)) {
                    String reqType = dataSnapshot.child(tappedUserKey).child("request_type").getValue().toString();
                    if (reqType.equals("sent")) {
                        CURRENT_STATE = "request_sent";
                        acceptBtn.setText("Cancle Friend Request");
                    }
                    if (reqType.equals("received")){
                        CURRENT_STATE = "request_received";
                        acceptBtn.setText("Accept Friend Request");
                        declineBtn.setVisibility(View.VISIBLE);
                        declineBtn.setEnabled(true);
                    }
                }else {
                    friendReference.child(currentUserKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                if (dataSnapshot.hasChild(tappedUserKey)){
                                    CURRENT_STATE = "friends";
                                    acceptBtn.setText("Unfriend");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Picasso.with(getApplicationContext()).load(picLink).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.defaultpic).into(circleImageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(ProfileActivity.this).load(picLink).placeholder(R.drawable.defaultpic).into(circleImageView);
            }
        });
        profileShowUsername.setText(username);
        profileShowStatus.setText(status);
    }
}
