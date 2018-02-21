package com.thad.sparsenavigation;

import android.content.Context;
import android.util.Log;

import com.thad.sparse_nav_lib.WarehouseMap;
import com.thad.sparsenavigation.Communications.CommunicationHandler;
import com.thad.sparsenavigation.UI.UserInterfaceHandler;
import com.thad.sparsenavigation.Utils.FileIO;

/**
 * Android Client coordinates all services (UI, I/O, Comms etc.)
 */

public class AndroidClient {
    private static final String TAG = "|AndroidClient|";

    //If QUICK_START is true the application immediately attempts to connect.
    private static final boolean QUICK_START = true;

    private UserInterfaceHandler mUI;
    private CommunicationHandler mCommHandler;
    private FileIO mFileIO;

    private WarehouseMap mMap;

    public AndroidClient(Context context){
        mUI = new UserInterfaceHandler(context, this);
        mCommHandler = new CommunicationHandler(this);
        init();
    }

    private void init(){
        if(QUICK_START){
            mCommHandler.connect();
        }
    }

    public void onDestroy(){
        mCommHandler.shutdown();
    }

    public void onMapReceived(WarehouseMap map){
        Log.i(TAG, "The Android Client received the Warehouse Map.");
        mMap = map;
        mUI.setMap(map);
    }
}
