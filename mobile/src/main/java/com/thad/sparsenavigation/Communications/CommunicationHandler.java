package com.thad.sparsenavigation.Communications;

import com.thad.sparse_nav_lib.Decoder;
import com.thad.sparse_nav_lib.WarehouseMap;
import com.thad.sparsenavigation.AndroidClient;

/**
 * Coordinates all communications with other devices (Glass, Server etc.)
 * Reports to the Android Client.
 */

public class CommunicationHandler {
    private static final String TAG = "|CommunicationHandler|";

    private AndroidClient mClient;

    private GlassHandler mGlassHandler;
    private ServerHandler mServerHandler;

    public CommunicationHandler(AndroidClient androidClient){
        mClient = androidClient;

        init();
    }

    private void init(){
        mGlassHandler = new GlassHandler(this);
        mServerHandler = new ServerHandler(this);
    }

    public void connect(){
        mGlassHandler.connect();
        mServerHandler.connect();
    }

    public void shutdown(){
        mGlassHandler.shutdown();
        mServerHandler.shutdown();
    }


    public void onMapReceived(String mapJSON){
        mGlassHandler.sendMap(mapJSON);

        WarehouseMap map = Decoder.decodeMap(mapJSON);
        mClient.onMapReceived(map);
    }
}
