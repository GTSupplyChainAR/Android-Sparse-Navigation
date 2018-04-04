package com.thad.locationtracker.UI;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.thad.locationtracker.AndroidClient;
import com.thad.locationtracker.R;
import com.thad.sparse_nav_lib.Static.Prefs;
import com.thad.sparse_nav_lib.UI.WarehouseMapView;
import com.thad.sparse_nav_lib.WarehouseLocation;
import com.thad.sparse_nav_lib.WarehouseMap;

/**
 * This Class handles the User Interface.
 * It mainly interacts with the MapView, but is also responsible for the rest of the UI.
 * It should overlay the Route over the MapView, the location being clicked etc.
 */

public class UserInterfaceHandler {
    private static final String TAG = "|UserInterfaceHandler|";

    private Activity mActivity;
    private AndroidClient mClient;

    private WarehouseMap mMap;
    private WarehouseMapView mMapView;

    private PickerView pickerView;


    public UserInterfaceHandler(final Context context, AndroidClient androidClient){
        mActivity = (Activity) context;
        mClient = androidClient;

        mMapView = new WarehouseMapView(mActivity);
        //mMapView.setSize(Prefs.SCREEN_WIDTH, Prefs.SCREEN_HEIGHT);

        pickerView = new PickerView(context,200,500);
        RelativeLayout layout = mActivity.findViewById(R.id.main_layout);
        layout.addView(mMapView, 0);
        layout.addView(pickerView, 1);


        mMapView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                int X = (int) event.getX();
                int Y = (int) event.getY();
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_MOVE:
                        WarehouseMapView mapView = (WarehouseMapView)v;
                        mapView.setLocationFromViewCoords(X,Y);
                        int[] pos = mapView.getRawLocation();
                        pickerView.setLocation(pos[0], pos[1]);
                        //pickerView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cursor));
                        pickerView.invalidate();
                        //Log.d(TAG, "Truth: "+X+","+Y);
                        //Log.d(TAG, "Approx: "+pos[0]+","+pos[1]);
                        break;
                }
                return true;
            }
        });




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

    public void changeBackground(){
        mMapView.changeBackground();
    }



    //This function returns the last known location of the user
    public WarehouseLocation getLastLocation(){
        return mMapView.getLocation();
    }


}
