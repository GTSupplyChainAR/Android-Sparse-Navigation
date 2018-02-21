package com.thad.locationtracker;

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
    private AndroidClient mClient;

    public CommunicationHandler(AndroidClient client) {
        mClient = client;
    }

    public void shutdown(){
        //Close sockets, databases etc.
    }

    public void sendLocation(WarehouseLocation loc){
        //Send Location via Firebase/Socket/...
    }

    public void receiveMap(){
        //Receive Map via Firebase/Socket/...
        WarehouseMap map = new WarehouseMap(10,16); //PLACEHOLDER
        mClient.onMapReceived(map);
    }
}
