package com.thad.sparsenavigation.Communications;

import android.util.Log;

/**
 * Handles all communications with the Server (Firebase, WebSockets, etc).
 *
 * Receives:
 * -> Warehouse Map
 * -> Pick Paths
 * -> Routes
 * -> Warehouse Location stream
 *
 * Reports to the CommunicationHandler.
 */

public class ServerHandler {
    private static final String TAG = "|ServerHandler|";

    private CommunicationHandler mCommHandler;

    public ServerHandler(CommunicationHandler communicationHandler){
        mCommHandler = communicationHandler;
        init();
    }

    private void init(){
        //Setup variables
    }

    public void connect(){
        //Initiate connection with Server
        Log.i(TAG, "Connecting with Server.");

        //PLACEHOLDER
        mCommHandler.onMapReceived("placeholderJSON");
    }

    public void shutdown(){
        //Close sockets, databases etc.
        Log.i(TAG, "Shutting Server connection down.");
    }

}
