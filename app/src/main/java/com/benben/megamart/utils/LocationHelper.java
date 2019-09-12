package com.benben.megamart.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import com.benben.megamart.config.Constants;


/**
 * Created by mxy on 2019/6/17.
 * 定位
 * 使用GPS和网络定位，如果在室内GPS获取不到会使用网络定位
 */

public class LocationHelper {

    private LocationManager locationManager;

    private OnLocationLoadedListener mOnLocationLoadedListener;

    private boolean mIsGetLocation = false;
    private MyLocationListener listeners[] = {
            new MyLocationListener(),
            new MyLocationListener()
    };

    public LocationHelper(Context context, OnLocationLoadedListener listener) {
        this.mOnLocationLoadedListener = listener;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

    }

    @SuppressLint("MissingPermission")
    public void startRequestLocationUpdates() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10f, listeners[0]);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10F, listeners[1]);
    }

    public void stopRequestLocationUpdates() {
        locationManager.removeUpdates(listeners[0]);
        locationManager.removeUpdates(listeners[1]);
    }

//    public Location getCurrentLocation() {
//
//        // go in best to worst order
//        for (int i = 0; i < listeners.length; i++) {
//            Location l = listeners[i].current();
//            if (l != null) return l;
//        }
//        Log.d(TAG, "No location received yet.");
//        return null;
//    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location newLocation) {
            if (newLocation.getLatitude() == 0.0
                    && newLocation.getLongitude() == 0.0) {
                // Hack to filter out 0.0,0.0 locations
                return;
            }
            Log.e(Constants.WHK_TAG,"the newLocation is " + newLocation.getLongitude() + "x" + newLocation.getLatitude());
            if(mOnLocationLoadedListener != null && newLocation != null && !mIsGetLocation){
                mIsGetLocation = true;
                mOnLocationLoadedListener.locationLoaded(newLocation);
                stopRequestLocationUpdates();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.OUT_OF_SERVICE:
                case LocationProvider.TEMPORARILY_UNAVAILABLE: {
                    break;
                }
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(Constants.WHK_TAG,"location=-=-=&&&&&&&Enabled==="+provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(Constants.WHK_TAG,"location=-=-=&&&&&&&Disabled==="+provider);
        }

    }

    public interface OnLocationLoadedListener{
        void locationLoaded(Location location);
    }

}
