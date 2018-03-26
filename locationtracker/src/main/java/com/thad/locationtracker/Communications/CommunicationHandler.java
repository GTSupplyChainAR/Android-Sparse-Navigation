package com.thad.locationtracker.Communications;

import android.util.Log;

import com.thad.locationtracker.AndroidClient;
import com.thad.sparse_nav_lib.Decoder;
import com.thad.sparse_nav_lib.Static.Prefs;
import com.thad.sparse_nav_lib.WarehouseLocation;
import com.thad.sparse_nav_lib.WarehouseMap;

/**
 * This class handles all the communications with the Server.
 *
 * Receives:
 * -> Warehouse Map
 * -> Active Route
 *
 * Sends:
 * -> Warehouse Location
 *
 * Use sparse_nav_lib/Decoder to encode/decode messages.
 */

public class CommunicationHandler {
    private static final String TAG = "|CommunicationHandler|";

    private AndroidClient mClient;
    private ServerBluetooth bluetooth;

    public CommunicationHandler(AndroidClient client) {
        mClient = client;
        bluetooth = new ServerBluetooth(this);
        bluetooth.setAddress(Prefs.GLASS_ADDRESS, Prefs.GLASS_UUID);
        bluetooth.listen();
    }

    public void shutdown(){
        //Close sockets, databases etc.
        bluetooth.disconnect();
    }

    public void sendLocation(WarehouseLocation loc){
        //Send Location via Firebase/Socket/...
        Log.d(TAG, "Sending -> "+loc.toString());
        bluetooth.sendMessage(Decoder.MSG_TAG.LOC, Decoder.encodeWarehouseLocation(loc));
    }

    public void receiveMap(){
        //Receive Map via Firebase/Socket/...
        WarehouseMap map = new WarehouseMap();
        mClient.onMapReceived(map);
    }

    public void onMessageReceived(Decoder.MSG_TAG tag, String msg){
        Log.d(TAG, "Message Received! "+msg);
    }

    public void onConnected(){
        Log.d(TAG, "On Connected.");
    }

    public void onConnectionLost(){
        Log.d(TAG, "On Connection Lost.");
    }
}
