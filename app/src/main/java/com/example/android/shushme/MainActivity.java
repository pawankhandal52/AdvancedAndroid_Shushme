package com.example.android.shushme;

/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    // Constants
    public static final String TAG = MainActivity.class.getSimpleName();

    // Member variables
    private PlaceListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private final static int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    /**
     * Called when the activity is starting
     *
     * @param savedInstanceState The Bundle that contains the data supplied in onSaveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.places_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PlaceListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        // TODO (4) Create a GoogleApiClient with the LocationServices API and GEO_DATA_API
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this,this)
                .build();
    }
    
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected: Api Client connection was succeessful");
    }
    
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "onConnectionSuspended: Api Cleint Suspened");
    }
    
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed: ");
    }
    
    // TODO (5) Override onConnected, onConnectionSuspended and onConnectionFailed for GoogleApiClient
    // TODO (7) Override onResume and inside it initialize the location permissions checkbox
    // TODO (8) Implement onLocationPermissionClicked to handle the CheckBox click event
    // TODO (9) Implement the Add Place Button click event to show  a toast message with the permission status
    
    @Override
    protected void onResume() {
        super.onResume();
        CheckBox checkBox = findViewById(R.id.permision_location_cb);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            checkBox.setChecked(false);
        }else{
            checkBox.setEnabled(false);
            checkBox.setChecked(true);
        }
        
    }
    
    public void onLocationPermissionClicked(View view) {
       /* if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this, "Please allow us to acess your location", Toast.LENGTH_SHORT).show();
            }else{
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
    
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            
        }else{
            Log.i(TAG, "onLocationPermissionClicked: Permision already granted");
        }*/
    
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_FINE_LOCATION);
    }
    
    public void addPlace(View view) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, getResources().getString(R.string.need_location_permission_message), Toast.LENGTH_SHORT).show();
            return;
        }else{
            Toast.makeText(this, getResources().getString(R.string.need_location_permission_granted_message), Toast.LENGTH_SHORT).show();
        }
    }
}
