package com.example.gyan.intouch.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.gyan.intouch.R;
import com.example.gyan.intouch.models.UserCardModel;
import com.example.gyan.intouch.viewHolders.UserCardModelViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private FirebaseRecyclerAdapter<UserCardModel, UserCardModelViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        setSupportActionBar((Toolbar) findViewById(R.id.allUser_appbar));
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.allUserRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.keepSynced(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserCardModel, UserCardModelViewHolder>
                (
                        UserCardModel.class,R.layout.all_user_card,UserCardModelViewHolder.class,reference
                ) {
            @Override
            protected void populateViewHolder(UserCardModelViewHolder viewHolder, final UserCardModel model, final int position) {
                viewHolder.updateUI(model.getUser_name(),model.getUser_status(),model.getUser_thumb_image(),getApplicationContext());

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String tappedUserKey = getRef(position).getKey();
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class).putExtra("userKey",tappedUserKey)
                        .putExtra("userStatus",model.getUser_status()).putExtra("userThumb",model.getUser_image()).putExtra("userName",model.getUser_name()));
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        firebaseRecyclerAdapter.cleanup();
    }
}
