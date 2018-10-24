package com.example.android.shushme;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pawan Khandal on 10/24/18,02
 */
public class GeoFencing implements ResultCallback {
    private final String TAG = GeoFencing.class.getSimpleName();
    private Context mContext;
    private List<Geofence> mGeofenceList;
    private GoogleApiClient mGoogleApiClient;
    private PendingIntent mGeofencePendingIntent;
    private final long GEOFENCE_TIMEOUT = 86400;
    private final float GEOFENCE_RADIOUS = 50;
    
    public GeoFencing(Context context, GoogleApiClient googleApiClient) {
        mContext = context;
        mGoogleApiClient = googleApiClient;
        mGeofenceList = new ArrayList<>();
        mGeofencePendingIntent = null;
        
    }
    
    public void updateGeofencesList(PlaceBuffer places) {
        mGeofenceList = new ArrayList<>();
        if (places == null || places.getCount() == 0) return;
        for (Place place : places) {
            String placeId = place.getId();
            double lat = place.getLatLng().latitude;
            double lon = place.getLatLng().longitude;
    
            Log.i(TAG, "updateGeofencesList: placeId = " + placeId + ", lat =" + lat + ", lon =" + lon);
            //Build Geofence object
            Geofence geofence = new Geofence.Builder().
                    setRequestId(placeId)
                    .setExpirationDuration(GEOFENCE_TIMEOUT)
                    .setCircularRegion(lat, lon, GEOFENCE_RADIOUS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();
            
            mGeofenceList.add(geofence);
        }
    
    }
    
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }
    
    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
    
        Intent intent = new Intent(mContext, GeofenceBroadcastReceiver.class);
        mGeofencePendingIntent = PendingIntent.getBroadcast(mContext, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }
    
    public void registerAllGeofences() {
        //Check api is connected and
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected() ||
                mGeofenceList == null || mGeofenceList.size() == 0) {
            return;
        }
    
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.GeofencingApi.addGeofences(mGoogleApiClient,
                getGeofencingRequest(),
                getGeofencePendingIntent()
        ).setResultCallback(this);
    }
    
    public void unRegisterAllGeofences(){
        if(mGoogleApiClient == null || !mGoogleApiClient.isConnected()){
            return;
        }
        
        LocationServices.GeofencingApi.removeGeofences(mGoogleApiClient
                ,getGeofencePendingIntent()).setResultCallback(this);
    }
    
    @Override
    public void onResult(@NonNull Result result) {
        Log.e(TAG, String.format("Error in adding/removing geofence: %s ",result.getStatus().toString()) );
    }   
}
