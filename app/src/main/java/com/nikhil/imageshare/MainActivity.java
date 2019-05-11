package com.nikhil.imageshare;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_ON_BUTTON_CLICK = 11;
    private static final int PERMISSIONS_REQUEST_TO_LOAD_IMAGE = 12;
    private Uri imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enter file path here
        String imagePath = "/storage/emulated/0/DCIM/Camera/share.jpg";
        File imageFile = new File(imagePath);
        imageURI = FileProvider.getUriForFile(
                MainActivity.this,
                "com.nikhil.imageshare.provider", // provider authority
                imageFile);

        if (hasExternalStoragePermission()) {
            loadImage();
        } else {
            requestPermission(PERMISSIONS_REQUEST_TO_LOAD_IMAGE);
        }

        findViewById(R.id.buttonShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShareButtonClicked();
            }
        });
    }

    private void onShareButtonClicked() {
        if (hasExternalStoragePermission())
            shareImage();
        else {
            requestPermission(PERMISSIONS_REQUEST_ON_BUTTON_CLICK);
        }
    }

    private void loadImage() {
        ((ImageView) findViewById(R.id.imageView)).setImageURI(imageURI);
    }

    private void shareImage() {
        if (imageURI != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageURI);
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, "Select App"));
        }
    }

    private boolean hasExternalStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(final int requestCode) {
        // Permission is not granted
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                    .setTitle("Storage Permission")
                    .setMessage("Storage permission is required to share image")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showRequestPopup(requestCode);
                        }
                    })
                    .setNegativeButton("Cancel", null);
            alertBuilder.create().show();
        } else {
            // No explanation needed; request the permission
            showRequestPopup(requestCode);

        }
    }

    private void showRequestPopup(int requestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case PERMISSIONS_REQUEST_ON_BUTTON_CLICK:
                    shareImage();
                    break;
                case PERMISSIONS_REQUEST_TO_LOAD_IMAGE:
                    loadImage();
                    break;
            }
        }
    }
}
