package com.bdreiss.trackmyattack;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.bdreiss.dataAPI.util.Coordinate;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

public class CurrentLocation {

    public interface LocationResultCallback {
        void onLocationResult(Coordinate location);

    }

    public static void getCurrentLocation(Context context, LocationResultCallback callback) {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permissions not granted, consider requesting here and handling asynchronously
            callback.onLocationResult(null);
            return;
        }

        fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
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
    private static Coordinate getLastLocation(Context context) {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        Coordinate[] coordinate = new Coordinate[1];
        coordinate[0] = null;

        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(location ->{
            coordinate[0] = new Coordinate(location.getLatitude(), location.getLatitude());

        });

        while (!task.isComplete()){

        }

        return coordinate[0];

    }

}
