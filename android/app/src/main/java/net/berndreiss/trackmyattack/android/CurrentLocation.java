package net.berndreiss.trackmyattack.android;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.content.Intent;
import android.provider.Settings;

import androidx.activity.result.ActivityResultLauncher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import androidx.core.app.ActivityCompat;

import net.berndreiss.trackmyattack.data.util.Coordinate;

import net.berndreiss.trackmyattack.android.sync.Synchronizer;

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

        // if no permission has been granted pass null to the callback and return
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && !(new net.berndreiss.trackmyattack.android.Settings(context).getDeniedLocationAccess())) {
            ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            callback.onLocationResult(null);
            return;
        }

        //prompt user to turn on GPS if not enabled finish getting location otherwise
        if (gpsNotEnabled(context)) {

            // show alert dialog -> if user decides to turn on GPS use the locationSettingsResultLauncher
            // finish getting location otherwise
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("GPS is not enabled. Do you want to go to settings menu?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                locationSettingsResultLauncher.launch(intent);
            });

            builder.setNegativeButton("No", (dialogInterface, i) -> finishGettingLocation(context, false));
            builder.show();

        } else
            finishGettingLocation(context, true);
    }

    //finishes getting location
    public static void finishGettingLocation(Context context, boolean getLocation) {

        if (gpsNotEnabled(context)) {
            callback.onLocationResult(null);
            return;
        }
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        // if no permission has been granted pass null to the callback and return
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            callback.onLocationResult(null);
            return;
        }

        //check whether device is online, if it is not, set synced in settings to false
        // and mark the sync button, synchronize otherwise
        if (!Synchronizer.isNetworkAvailable(context) && getLocation)
            showInternetPrompt(context, result -> {
                if (result)
                    if (Synchronizer.isNetworkAvailable(context))
                        goToFused(context, fusedLocationProviderClient);
            });

        goToFused(context, fusedLocationProviderClient);

    }
    public static void goToFused(Context context, FusedLocationProviderClient fusedLocationProviderClient) {

        if (!Synchronizer.isNetworkAvailable(context)){
            callback.onLocationResult(null);
            return;
        }

        //get the last location and pass it to the callback, if location is null, pass null
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        builder.setMessage("This app requires an internet connection to access your location. Please turn on mobile network or Wi-Fi in Settings.");
        builder.setPositiveButton("OK", (dialog, which) -> callback.onResult(true));
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
            callback.onResult(false);
        });
        builder.show();
    }

    public interface Callback {
        void onResult(boolean result);
    }

    private static boolean gpsNotEnabled(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        boolean gpsEnabled = false;

        //try whether GPS is enabled and ignore exceptions
        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ignored) {
        }
        return !gpsEnabled;
    }

}
