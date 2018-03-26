package com.thad.locationtracker;

import android.content.Context;

import com.thad.locationtracker.Communications.CommunicationHandler;
import com.thad.locationtracker.UI.UserInterfaceHandler;
import com.thad.sparse_nav_lib.WarehouseLocation;
import com.thad.sparse_nav_lib.WarehouseMap;

/**
 * Android Client coordinates all services (UI, I/O, Comms etc.)
 */

public class AndroidClient {
    private static final String TAG = "|AndroidClient|";

    private WarehouseMap mMap;
    private UserInterfaceHandler mUI;
    private CommunicationHandler mCommHandler;

    private LocationThread locationThread;

    public AndroidClient(Context context){
        mUI = new UserInterfaceHandler(context, this);
        mCommHandler = new CommunicationHandler(this);
        locationThread = new LocationThread();


        // PLACEHOLDER
        mCommHandler.receiveMap();
    }

    public void onDestroy(){
        mCommHandler.shutdown();
    }

    //This function is called when the Map is received by the Communication Handler
    public void onMapReceived(WarehouseMap map){
        mMap = map;
        mUI.setMap(mMap);
        locationThread.start();
    }

    /**
     * This Thread runs in the background.
     * It starts after the Map is received, and keeps going from there on.
     * It sends a new Location at set intervals (broadcast_frequency)
     */
    private class LocationThread extends Thread{
        private final Long broadcast_frequency = Long.valueOf(100);
        private Long prev_send_time;

        public void run(){
            prev_send_time = System.currentTimeMillis();
            while(true) {
                Long curr_time = System.currentTimeMillis();
                if (curr_time - prev_send_time >= broadcast_frequency) {
                    WarehouseLocation loc = mUI.getLastLocation();
                    mCommHandler.sendLocation(loc);
                    prev_send_time = curr_time;
                }
            }
        }
    }
}
