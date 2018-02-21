package com.thad.locationtracker.UI;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;

import com.thad.locationtracker.AndroidClient;
import com.thad.locationtracker.R;
import com.thad.sparse_nav_lib.UI.WarehouseMapView;
import com.thad.sparse_nav_lib.WarehouseLocation;
import com.thad.sparse_nav_lib.WarehouseMap;

/**
 * This Class handles the User Interface.
 * It mainly interacts with the MapView, but is also responsible for the rest of the UI.
 * It should overlay the Route over the MapView, the location being clicked etc.
 */

public class UserInterfaceHandler {

    private Activity mActivity;
    private AndroidClient mClient;

    private WarehouseMap mMap;
    private WarehouseMapView mMapView;

    public UserInterfaceHandler(Context context, AndroidClient androidClient){
        mActivity = (Activity) context;
        mClient = androidClient;

        mMapView = new WarehouseMapView(mActivity);
        RelativeLayout layout = mActivity.findViewById(R.id.main_layout);
        layout.addView(mMapView);
    }

    //This function is called when the Map is received from the Server
    public void setMap(WarehouseMap map){
        mMap = map;

        //Generate UI elements that represent the map
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMapView.setMap(mMap);
                mMapView.generateUI();
            }
        });
    }

    //This function returns the last known location of the user
    public WarehouseLocation getLastLocation(){
        return mMapView.getLocation();
    }


}
