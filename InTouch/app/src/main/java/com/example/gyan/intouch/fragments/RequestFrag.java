package com.example.gyan.intouch.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gyan.intouch.Adapters.RequestFragAdapter;
import com.example.gyan.intouch.R;
import com.example.gyan.intouch.models.FriendRequestModel;
import com.example.gyan.intouch.viewHolders.FriendRequestCardViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestFrag extends Fragment {

    private RecyclerView recyclerView;
    private View view;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<FriendRequestModel , FriendRequestCardViewHolder> firebaseRecyclerAdapter;
    private List<String> userIds = new ArrayList<>();
    private RequestFragAdapter requestFragAdapter;

    public String current_user_id;

    public RequestFrag() {
        // Required empty public constructor
    }

    public void fetchRequests(){
        Query query = databaseReference.orderByChild("request_type").equalTo("received");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userIds.clear();
                for (DataSnapshot requests : dataSnapshot.getChildren()){
                    userIds.add(requests.getKey());
                }
                requestFragAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_request, container, false);

        current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Friend_Requests").child(current_user_id);

        requestFragAdapter = new RequestFragAdapter(userIds);
        recyclerView = (RecyclerView) view.findViewById(R.id.allFriendRequestRecycler);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(requestFragAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        fetchRequests();
    }
}
