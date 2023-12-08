package com.artsolo.goods;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ImageSetter {

    private final Activity activity;
    private final Context context;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int MEDIA_PERMISSION_CODE = 102;

    public ImageSetter(Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
    }

    public void setImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (mediaIsPermit()) {
                loadAndCropImage(activity);
            } else {
                requestMediaPermission();
            }
        } else {
            if (storageIsPermit()) {
                loadAndCropImage(activity);
            } else {
                requestStoragePermission();
            }
        }

    }

    public void loadAndCropImage(Activity activity) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(activity);
    }

    public void handleImageActivityResult(int requestCode, int resultCode, Intent data, ImageView imageVive) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri croppedImageUri = result.getUri();
                imageVive.setImageURI(croppedImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean mediaIsPermit() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == (PackageManager.PERMISSION_GRANTED);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestMediaPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_MEDIA_IMAGES} , MEDIA_PERMISSION_CODE);
    }

    private boolean storageIsPermit() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , STORAGE_PERMISSION_CODE);
    }
}
