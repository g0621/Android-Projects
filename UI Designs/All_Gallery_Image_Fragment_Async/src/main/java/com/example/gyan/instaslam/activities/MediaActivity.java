package com.example.gyan.instaslam.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gyan.instaslam.R;
import com.example.gyan.instaslam.models.InstaImage;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MediaActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    // BY ME DOWN
    ImageView mainImage;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 1 :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchAndSetMedia();
                }
        }
    }

    private ArrayList<InstaImage> arrayList = new ArrayList<>();

    public void fetchAndSetMedia(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
                if (cursor != null){
                    arrayList.clear();
                    cursor.moveToFirst();
                    for (int x =  0;x < cursor.getCount() ; x++){
                        cursor.moveToPosition(x);
                        Log.i("Donkey","URI" + cursor.getString(1));
                        InstaImage instaImage = new InstaImage(Uri.parse(cursor.getString(1)));
                        arrayList.add(instaImage);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //set Images on recycler view Adapter
                    }
                });
            }
        });
    }
    //BY ME UP
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_media);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls); //linear layout
        mContentView = findViewById(R.id.fullscreen_content);    //textview


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        //BY ME DOWN
        mainImage = (ImageView) findViewById(R.id.selected_image);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.contet_image);
        ImageAdapter imageAdapter = new ImageAdapter(arrayList);
        recyclerView.setAdapter(imageAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getBaseContext(),4);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else {
            fetchAndSetMedia();
        }
        //BY ME UP

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    //BY ME DOWN

    public class ImageAdapter extends RecyclerView.Adapter<MainHolder>{

        private ArrayList<InstaImage> imageArray;

        public ImageAdapter(ArrayList<InstaImage> arrayList) {
            this.imageArray = arrayList;
        }

        @Override
        public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardimage,parent,false);
            return new MainHolder(card);
        }

        @Override
        public void onBindViewHolder(final MainHolder holder, int position) {
            final InstaImage instaImage = imageArray.get(position);
            holder.updateUI(instaImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //update the main image on top
                    mainImage.setImageDrawable(holder.imageView.getDrawable());
                }
            });
        }

        @Override
        public int getItemCount() {
            return imageArray.size();
        }
    }
    public class MainHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public MainHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.card_image);
        }

        public void updateUI(InstaImage image){
            //grab the uri and set it to the cards
            DecodeImage task = new DecodeImage(imageView,image);
            task.execute();
        }
    }
    public class DecodeImage extends AsyncTask<Void , Void ,Bitmap>{

        private final WeakReference<ImageView> imageViewWeakReference;
        private InstaImage instaImage;

        public DecodeImage(ImageView imageView, InstaImage instaImage) {
            this.imageViewWeakReference = new WeakReference<ImageView>(imageView);
            this.instaImage = instaImage;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return decodeURI(instaImage.getImageResourceURL().getPath());
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            final ImageView img = imageViewWeakReference.get();
            if (img != null) img.setImageBitmap(bitmap);   //always check because its async and user can terminate main thread

        }
    }

    public Bitmap decodeURI(String filePath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath , options);

        Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math.abs(options.outWidth - 100);
        if (options.outHeight * options.outWidth * 2  >= 16384){
            double sampleSize = scaleByHeight ? options.outHeight / 1000 : options.outWidth / 1000;
            options.inSampleSize = (int) Math.pow(2d ,Math.floor(Math.log(sampleSize)/Math.log(2d)));
        }
        options.inJustDecodeBounds = false;
        options.inTempStorage = new  byte[512];
        Bitmap output = BitmapFactory.decodeFile(filePath , options);
        return output;
    }

    //BY ME UP
}
