package com.example.gyan.instaslam.models;

import android.net.Uri;

/**
 * Created by Gyan on 9/26/2017.
 */

public class InstaImage {
    private Uri imageResourceURL;

    public InstaImage(Uri imageResourceURL) {
        this.imageResourceURL = imageResourceURL;
    }

    public Uri getImageResourceURL() {
        return imageResourceURL;
    }
}
