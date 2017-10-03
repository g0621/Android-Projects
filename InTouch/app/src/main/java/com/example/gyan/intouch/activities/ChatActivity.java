package com.example.gyan.intouch.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gyan.intouch.Adapters.ChatMessegeAdapter;
import com.example.gyan.intouch.Adapters.TimeResolver;
import com.example.gyan.intouch.R;
import com.example.gyan.intouch.models.ChatMessege;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String tappedKey,username,currentKey;
    private TextView UsernameView,LastSeenView;
    private CircleImageView chatProfileView;
    private DatabaseReference reference;
    private EditText MessegeView;
    private RecyclerView recyclerView;
    private List<ChatMessege> messegeList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private ChatMessegeAdapter chatMessegeAdapter;


    private void tost(View view, String s) {
        Snackbar.make(view, s, Snackbar.LENGTH_SHORT).show();
    }

    public void sendMessege(View view){
        String messege = MessegeView.getText().toString();
        if (TextUtils.isEmpty(messege))tost(view,"Really nothing to say 🤔🤔");
        else {
            String sender_ref = "Messages/" + currentKey + "/" + tappedKey;
            String receiver_ref = "Messages/" + tappedKey + "/" + currentKey;

            DatabaseReference messege_ref = reference.child("Messages").child(currentKey).child(tappedKey).push();
            String messege_Key = messege_ref.getKey();




            Map messageBody = new HashMap();
            messageBody.put("message",messege);
            messageBody.put("seen",false);
            messageBody.put("type","text");
            messageBody.put("time", ServerValue.TIMESTAMP);
            messageBody.put("from",FirebaseAuth.getInstance().getCurrentUser().getUid());

            Map Messege_body_details  = new HashMap();

            Messege_body_details.put(sender_ref + "/" + messege_Key, messageBody);
            Messege_body_details.put(receiver_ref + "/" + messege_Key, messageBody);

            reference.updateChildren(Messege_body_details, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null){
                        Log.i("chat Error",databaseError.getMessage());
                    }
                    MessegeView.setText("");
                }
            });
        }
    }

    private void fetchMessege(){
        reference.child("Messages").child(currentKey).child(tappedKey)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        ChatMessege chatMessege = dataSnapshot.getValue(ChatMessege.class);
                        messegeList.add(chatMessege);
                        chatMessegeAdapter.notifyDataSetChanged();
			recyclerView.smoothScrollToPosition(chatMessegeAdapter.getItemCount() - 1);

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        Toolbar ChatToolbar = (Toolbar) findViewById(R.id.chatapp_bar);
        setSupportActionBar(ChatToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar  = layoutInflater.inflate(R.layout.chat_headbar,null);
        actionBar.setCustomView(action_bar);

        chatMessegeAdapter = new ChatMessegeAdapter(messegeList);
        linearLayoutManager = new LinearLayoutManager(this);

        UsernameView = (TextView) findViewById(R.id.chat_user_name);
        LastSeenView = (TextView) findViewById(R.id.chat_user_lastseen);
        chatProfileView = (CircleImageView) findViewById(R.id.chat_user_pic);
        MessegeView = (EditText) findViewById(R.id.chat_messege);
        recyclerView = (RecyclerView) findViewById(R.id.chat_recycler);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatMessegeAdapter);

        currentKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Intent intent = getIntent();
        username = intent.getStringExtra("userName");
        tappedKey = intent.getStringExtra("userKey");
        final String thumb = intent.getStringExtra("userThumb");

        UsernameView.setText(username);
        Picasso.with(getApplicationContext()).load(thumb).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.defaultpic).into(chatProfileView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(getApplicationContext()).load(thumb).placeholder(R.drawable.defaultpic).into(chatProfileView);
            }
        });


        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(tappedKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String lastSeen = dataSnapshot.child("online").getValue().toString();
                if (lastSeen.equals("true")){
                    LastSeenView.setText("online");
                }
                else{
                    TimeResolver timeResolver = new TimeResolver();
                    long lastTime = Long.parseLong(lastSeen);
                    LastSeenView.setText("last seen " + timeResolver.getTimeAgo(lastTime));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fetchMessege();

    }
}
