package com.thad.sparsenavigation.Communications;

import android.util.Log;

import com.thad.sparse_nav_lib.Decoder;
import com.thad.sparse_nav_lib.Static.Prefs;
import com.thad.sparsenavigation.Communications.ClientBluetooth;
import com.thad.sparsenavigation.GlassClient;


/**
 * This Class handles the communications between Mobile App and glass.
 * It decodes the messages received on Bluetooth and populates the data structures.
 */

//TO DO
// Check for errors in Picking Data format

public class CommunicationHandler {
    private static final String TAG = "|CommunicationHandler|";

    private GlassClient mClient;

    private ClientBluetooth bluetooth;

    public CommunicationHandler(GlassClient client){
        mClient = client;
        bluetooth = new ClientBluetooth(this);
        bluetooth.setAddress(Prefs.PHONE_ADDRESS, Prefs.GLASS_UUID);

        bluetooth.connect();
    }

    public void shutdown(){
        bluetooth.disconnect();
    }


    public void onMessageReceived(Decoder.MSG_TAG msgTag, String msgString) {
        switch (msgTag){
            case MAP:
                Log.d(TAG, "Received the Warehouse Map.");
                //mClient.startExperiment(false);
                break;
            case LOC:
                Log.d(TAG, "Received a new location. "+msgString);
                mClient.onLocationUpdate(Decoder.decodeWarehouseLocation(msgString));
                break;
        }
    }

    public void onConnected(){mClient.onConnected();}
    public void onConnectionLost(){
        mClient.onConnectionLost();
    }
}
