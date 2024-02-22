package com.bdreiss.trackmyattack;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import androidx.core.app.ActivityCompat;

import com.bdreiss.dataAPI.util.Coordinate;
import com.bdreiss.trackmyattack.sync.Synchronizer;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import javax.security.auth.callback.Callback;

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
        } catch (Exception ignored) {
        }

        //prompt user to turn on GPS if not enabled finish getting location otherwise
        if (!gpsEnabled) {

            // show alert dialog -> if user decides to turn on GPS use the locationSettingsResultLauncher
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
    public static void finishGettingLocation(Context context) {

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        // if no permission has been granted pass null to the callback and return
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permissions not granted, consider requesting here and handling asynchronously
            callback.onLocationResult(null);
            return;
        }

        //check whether device is online, if it is not, set synced in settings to false
        // and mark the sync button, synchronize otherwise
        if (Synchronizer.isNetworkAvailable(context)) {

            showInternetPrompt(context, result -> {
                if (result) {
                    ConnectivityManager cm1 = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo1 = cm1.getActiveNetworkInfo();

                    if (Synchronizer.isNetworkAvailable(context)) {
                        goToFused(context, fusedLocationProviderClient);

                    }

                }
            });

        }

        goToFused(context, fusedLocationProviderClient);


    }

    public static void goToFused(Context context, FusedLocationProviderClient fusedLocationProviderClient) {
        //get the last location and pass it to the callback, if location is null, pass null
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {

                    if (location == null) {
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


                    } else {

                        Coordinate coordinate = new Coordinate(location.getLatitude(), location.getLongitude());
                        callback.onLocationResult(coordinate);
                        // Handle null location, possibly by using getLastLocation or notifying the callback
                    }
                })
                .addOnFailureListener(e -> {
                            // Handle failure case
                            callback.onLocationResult(null);
                        }
                );
    }

    public static void showInternetPrompt(final Context context, Callback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Internet Connection Required");
        builder.setMessage("This app requires an internet connection. Please turn on mobile network or Wi-Fi in Settings.");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                callback.onResult(true);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                callback.onResult(false);
            }
        });
        builder.show();
    }

    public interface Callback {
        void onResult(boolean result);
    }


}
