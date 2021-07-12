package com.example.drinkup.GestioneFile;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MyPermission { //permessi lettura/scrittura memoria esterna

    public static final int REQUEST_ID_READ_PERMISSION = 100;
    public static final int REQUEST_ID_WRITE_PERMISSION = 200;


    // With Android Level >= 23, you have to ask the user
    // for permission with device (For example read/write data on the device).
    public static boolean askPermission(AppCompatActivity activity, int requestId, String permissionName) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Check if we have permission
            int permission = ActivityCompat.checkSelfPermission(activity, permissionName);


            if (permission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                activity.requestPermissions(
                        new String[]{permissionName},
                        requestId
                );
                return false;
            }
        }
        return true;
    }

    public static boolean askWritePermission(AppCompatActivity mainActivity) {
        return askPermission(mainActivity, REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static boolean askReadPermission(AppCompatActivity mainActivity) {
        return askPermission(mainActivity, REQUEST_ID_READ_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }
}

