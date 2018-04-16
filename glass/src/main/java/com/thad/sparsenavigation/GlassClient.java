package com.thad.sparsenavigation;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.thad.sparse_nav_lib.PickRoute;
import com.thad.sparse_nav_lib.WarehouseLocation;
import com.thad.sparse_nav_lib.WarehouseMap;
import com.thad.sparsenavigation.Communications.CommunicationHandler;
import com.thad.sparsenavigation.Graphics.GLRenderer;
import com.thad.sparsenavigation.UI.UserInterfaceHandler;

/**
 * Created by theo on 3/25/18.
 */

public class GlassClient {
    private static final String TAG = "|GlassClient|";

    private Context context;
    private WarehouseMap mMap;

    private GLRenderer mRenderer;
    private UserInterfaceHandler mUI;
    private CommunicationHandler mCommHandler;
    private SensorListener mSensorListener;

    public GlassClient(Context context){
        this.context = context;
        init();
    }

    private void init(){
        mRenderer = new GLRenderer(this);

        mSensorListener = new SensorListener(this);
        mUI = new UserInterfaceHandler(this);
        mCommHandler = new CommunicationHandler(this);

        mMap = new WarehouseMap();
        mUI.setMap(mMap);
    }

    public void glassTapped(){
        Log.d(TAG, "Glass Tapped");
        PickRoute nextRoute = mCommHandler.getNextPickRoute();
        if(nextRoute == null)
            return;

        mUI.setRoute(nextRoute);
        mRenderer.setRoute(nextRoute);
    }

    public void addVerticalShelfView(){
        mUI.addVerticalShelfView();
    }

    public void deleteVerticalShelfView(){
        mUI.deleteVerticalShelfView();
    }

    public void resume(){ mSensorListener.resume(); }
    public void pause(){ mSensorListener.pause(); }

    public void onSensorUpdate(float heading){onSensorUpdate(heading, false);}
    public void onSensorUpdate(float heading, boolean isFake){
        if(isFake)
            mRenderer.addHeading(heading);
        else
            mRenderer.setHeading(heading);
        //mUI.onHeadingChanged(heading, isFake);
    }

    public void onLocationUpdate(WarehouseLocation loc){
        Log.d(TAG, "Received "+loc.toString());
        //mUI.onLocationUpdate(loc);
        mRenderer.setLocation(loc);
    }

    public PickRoute getNextPickRoute(){
        return mCommHandler.getNextPickRoute();
    }

    public void onConnected(){}
    public void onConnectionLost(){}
    // update firebase
    public Context getContext(){ return context; }
    public GLRenderer getRenderer(){ return mRenderer; }
}
