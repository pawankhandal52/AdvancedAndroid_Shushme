package com.example.android.shushme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Pawan Khandal on 10/24/18,46
 */
public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private final String TAG = GeofenceBroadcastReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");
    }
}
