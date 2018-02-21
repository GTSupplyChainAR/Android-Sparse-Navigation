package com.thad.sparsenavigation.Communications;

import android.util.Log;

/**
 * Handles all communications with the Glass
 *
 * Sends:
 * -> Warehouse Map
 * -> Next Book
 * -> Active Route
 * -> Warehouse Location Stream
 *
 * Reports to the CommunicationHandler.
 */

public class GlassHandler {
    private static final String TAG = "|GlassHandler|";

    private CommunicationHandler mCommHandler;

    public GlassHandler(CommunicationHandler communicationHandler){
        mCommHandler = communicationHandler;
        init();
    }

    private void init(){
        //Setup variables
    }

    public void connect(){
        //Initiate connection with Glass
        Log.i(TAG, "Connecting with Glass.");
    }

    public void sendMap(String encodedMap){
        //Send to Glass
    }

    public void shutdown(){
        //Close sockets, databases etc.
        Log.i(TAG, "Shutting Glass connection down.");
    }

}