package com.example.gyan.intouch.viewHolders;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gyan.intouch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Gyan on 10/2/2017.
 */

public class FriendRequestCardViewHolder extends RecyclerView.ViewHolder {

    public View view;
    private String currentUserKey,tappedUserKey,Username;
    private DatabaseReference friendRequestRefrence,friendReference;

    public FriendRequestCardViewHolder(View itemView) {
        super(itemView);

        view = itemView;
    }
    private void tost(View view, String s) {
        Snackbar.make(view, s, Snackbar.LENGTH_LONG).show();
    }

    private void decline(final int i){
        friendRequestRefrence.child(currentUserKey).child(tappedUserKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    friendRequestRefrence.child(tappedUserKey).child(currentUserKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                if (i == 0){
                                    tost(view,Username + " removed from list");
                                }
                                if (i == 1){
                                    tost(view,"Now friend with " + Username);
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void acceptRequest(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-YYYY");
        final String date = currentdate.format(calendar.getTime());
        friendReference.child(currentUserKey).child(tappedUserKey).child("date").setValue(date).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                friendReference.child(tappedUserKey).child(currentUserKey).child("date").setValue(date).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        decline(1);
                    }
                });
            }
        });
    }

    public void updateUI(String name, String status , final String thumb, final Context context,final String userKey){

        ((TextView) view.findViewById(R.id.reqcard_user_name)).setText(name);
        ((TextView) view.findViewById(R.id.reqcard_user_status)).setText(status);
         Picasso.with(context).load(thumb).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.defaultpic).into((CircleImageView) view.findViewById(R.id.reqcard_user_pic), new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(context).load(thumb).placeholder(R.drawable.defaultpic).into((CircleImageView) view.findViewById(R.id.reqcard_user_pic));
            }
        });

        tappedUserKey = userKey;
        currentUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        friendRequestRefrence = FirebaseDatabase.getInstance().getReference().child("Friend_Requests");
        friendRequestRefrence.keepSynced(true);
        friendReference = FirebaseDatabase.getInstance().getReference().child("Friends");
        friendReference.keepSynced(true);
        Username = name;

        ((Button) view.findViewById(R.id.all_req_list_accpt_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptRequest();
            }
        });

        ((Button)view.findViewById(R.id.all_req_list_dcln_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decline(0);
            }
        });

    }
}
