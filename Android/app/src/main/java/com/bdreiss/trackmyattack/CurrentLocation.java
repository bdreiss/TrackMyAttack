package com.bdreiss.trackmyattack;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.content.Intent;
import android.provider.Settings;
import androidx.activity.result.ActivityResultLauncher;

import androidx.appcompat.app.AlertDialog;

import androidx.core.app.ActivityCompat;

import com.bdreiss.dataAPI.util.Coordinate;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class CurrentLocation {

    public interface LocationResultCallback {
        void onLocationResult(Coordinate location);

    }

    public static LocationResultCallback callback;




    public static void getLocation(Context context, ActivityResultLauncher<Intent> locationSettingsResultLauncher) {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;

        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ignored) {}


        if(!gpsEnabled) {

            // GPS not enabled, prompt user to enable GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("GPS is not enabled. Do you want to go to settings menu?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                locationSettingsResultLauncher.launch(intent);
            });

            builder.setNegativeButton("No", (dialogInterface, i) -> {finishGettingLocation(context);});
            builder.show();

        } else
            finishGettingLocation(context);
    }

    public static void finishGettingLocation(Context context){
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permissions not granted, consider requesting here and handling asynchronously
            callback.onLocationResult(null);
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        Coordinate coordinate = new Coordinate(location.getLatitude(), location.getLongitude());
                        callback.onLocationResult(coordinate);
                    } else {
                        // Handle null location, possibly by using getLastLocation or notifying the callback
                        callback.onLocationResult(null);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure case
                    callback.onLocationResult(null);
                });


    }

}
