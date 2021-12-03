package com.mob3000.cinematrum.helpers;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationTracker extends Service implements LocationListener {

    private static int LOCATION_CHANGED_MIN_DISTANCE = 10; // 10 meters
    private static int LOCATION_CHANGED_MIN_TIME = 1000 * 60; // 1 minute
    private double longitude;
    private double latitude;
    private Location _location;
    private LocationManager _locationManager;
    private boolean networkEnabled;
    private boolean gpsEnabled;
    private Context ctx;
    private LocationListener _listener;

    public LocationTracker(Context ctx, LocationListener listener) {
        try{
            this.ctx = ctx;
            this._listener = listener;
            _locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
            checkPermissions();
            getLocation();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }

    private void getLocation() {
        try {

            if (ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) ctx, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }

            if (checkPermissions()) {

                if (gpsEnabled){
                    _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_CHANGED_MIN_TIME, LOCATION_CHANGED_MIN_DISTANCE, this);
                }
                else if (networkEnabled){
                    _locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_CHANGED_MIN_TIME, LOCATION_CHANGED_MIN_DISTANCE, this);
                }

                if (_locationManager != null) {
                    _location = _locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (_location != null) {
                        latitude = _location.getLatitude();
                        longitude = _location.getLongitude();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  boolean checkPermissions() {

        if (ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) ctx, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }

        gpsEnabled = _locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        networkEnabled = _locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        return gpsEnabled || networkEnabled;
        return ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && gpsEnabled;
    }

    public void stopTracking(){
        if (this._locationManager != null)
            _locationManager.removeUpdates(this);
    }


    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this._location = location;

        if (this._listener != null){
            this._listener.onLocationChanged(location);
        }

    }

    public void removeListener(){
        this.stopTracking();
        this._listener = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
