package com.example.gyan.intouch.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.gyan.intouch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class AccountSettingActivity extends AppCompatActivity {

    private CircleImageView userProfilePic;
    private TextView AccountUser, AccountStatus;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private StorageReference thumbStorageReference;
    Bitmap thumb_bitmap = null;
    private String currentUserId;

    private void tost(View view, String s) {
        Snackbar.make(view, s, Snackbar.LENGTH_LONG).show();
    }

    public void changeDisplayPic(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                tost(AccountUser,"Uploading Image...");
                Uri resultUri = result.getUri();
                File thumb = new File(resultUri.getPath());
                try {
                    thumb_bitmap = new Compressor(getApplicationContext()).setMaxHeight(200).setMaxHeight(200).setQuality(50).compressToBitmap(thumb);
                }catch (IOException e){
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();

                final StorageReference filepath = storageReference.child(currentUserId + ".jpg");
                final StorageReference filepath1 = thumbStorageReference.child(currentUserId + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task1) {
                        if (task1.isSuccessful()) {
                            final String imageURL = task1.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = filepath1.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    String thumbURL = task.getResult().getDownloadUrl().toString();
                                    if (task.isSuccessful()){
                                        Map imageUpdate = new HashMap();
                                        imageUpdate.put("user_image",imageURL);
                                        imageUpdate.put("user_thumb_image",thumbURL);
                                        reference.updateChildren(imageUpdate).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                tost(AccountUser,"Profile pic changed 🙂");
                                            }
                                        });
                                    }
                                }
                            });

                           // reference.child("user_thumb_image").setValue("defaultpic");
                        } else {
                            tost(AccountUser, "Error please try again ☹ ");
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                tost(AccountUser,result.getError().getMessage());
            }
        }
    }

    public void changeStatus(View view) {
        startActivity(new Intent(getApplicationContext(),StatusChangeActivity.class).putExtra("status",AccountStatus.getText().toString()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        setSupportActionBar((Toolbar) findViewById(R.id.account_setting_toolbar));
        getSupportActionBar().setTitle("Account Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.keepSynced(true);
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile_img");
        thumbStorageReference = FirebaseStorage.getInstance().getReference().child("Thumb_img");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userProfilePic = (CircleImageView) findViewById(R.id.AccountProfilePic);
        AccountUser = (TextView) findViewById(R.id.AccountuserName);
        AccountStatus = (TextView) findViewById(R.id.AccountuserStatus);

        reference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AccountStatus.setText(dataSnapshot.child("user_status").getValue().toString());
                AccountUser.setText(dataSnapshot.child("user_name").getValue().toString());
                final String image = dataSnapshot.child("user_image").getValue().toString();
                Picasso.with(AccountSettingActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.defaultpic).into(userProfilePic, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        Picasso.with(AccountSettingActivity.this).load(image).placeholder(R.drawable.defaultpic).into(userProfilePic);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
