package com.bdreiss.trackmyattack;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import androidx.core.app.ActivityCompat;

import com.bdreiss.dataAPI.util.Coordinate;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

//class implementing methods for getting current location from user
public class CurrentLocation {

    //interface used for determining what to do with location
    public interface LocationResultCallback {
        void onLocationResult(Coordinate location);

    }

    //callback used for determining what to do when returning from settings
    public static LocationResultCallback callback;

    //gets the location
    public static void getLocation(Context context, ActivityResultLauncher<Intent> locationSettingsResultLauncher) {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;

        //try whether GPS is enabled and ignore exceptions
        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ignored) {}

        //prompt user to turn on GPS if not enabled finish getting location otherwise
        if(!gpsEnabled) {

            // show alert dialog -> if user decides to turn on GPS use the lcoationSettingsResultLauncher
            // finish getting location otherwise
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("GPS is not enabled. Do you want to go to settings menu?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                locationSettingsResultLauncher.launch(intent);
            });

            builder.setNegativeButton("No", (dialogInterface, i) -> finishGettingLocation(context));
            builder.show();

        } else
            finishGettingLocation(context);
    }

    //finishes getting location
    public static void finishGettingLocation(Context context){

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        // if no permission has been granted pass null to the callback and return
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permissions not granted, consider requesting here and handling asynchronously
            callback.onLocationResult(null);
            return;
        }

        //get the last location and pass it to the callback, if location is null, pass null
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {

                if (location == null){
                    Log.d("XXX", "GET CURRENT");
                    //get the last location and pass it to the callback, if location is null, pass null
                    fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                                @Override
                                public boolean isCancellationRequested() {
                                    return false;
                                }

                                @NonNull
                                @Override
                                public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                                    return null;
                                }
                            }).addOnSuccessListener(location1 -> {

                                    if (location1 != null) {
                                        Coordinate coordinate = new Coordinate(location1.getLatitude(), location1.getLongitude());
                                        callback.onLocationResult(coordinate);
                                    } else {
                                        // Handle null location, possibly by using getLastLocation or notifying the callback
                                        callback.onLocationResult(null);
                                    }
                            })
                            .addOnFailureListener(e -> {
                                        // Handle failure case
                                        callback.onLocationResult(null);
                                    }
                            );


                }

                else{
                    Log.d("XXX", "GOT LAST");
                if (location != null) {
                        Coordinate coordinate = new Coordinate(location.getLatitude(), location.getLongitude());
                        callback.onLocationResult(coordinate);
                    } else {
                        // Handle null location, possibly by using getLastLocation or notifying the callback
                        callback.onLocationResult(null);
                    }
                }
        })
                .addOnFailureListener(e -> {
                    // Handle failure case
                    callback.onLocationResult(null);
                }
                );


    }

}
