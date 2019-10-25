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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by theo on 3/25/18.
 */

public class GlassClient {
    private static final String TAG = "|GlassClient|";

    private Context context;
    //private WarehouseMap mMap;

    private GLRenderer mRenderer;
    private UserInterfaceHandler mUI;
    private CommunicationHandler mCommHandler;
    private SensorListener mSensorListener;

    private long lasttime = 0;
    private long time;

    private String[] userIdPhasePathId = new String[]{"", "", ""};
    private String[] lastUserIdPhasePathId = new String[]{"", "", ""};
    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
    //String format;
    private int bookOrderInPath = 100;

    public GlassClient(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        mRenderer = new GLRenderer(this);

        mSensorListener = new SensorListener(this);
        mUI = new UserInterfaceHandler(this);
        mCommHandler = new CommunicationHandler(this);
        mCommHandler.readData(context);


        //mMap = new WarehouseMap();
        //mUI.setMap(mMap);
    }

    public void glassTapped() {
        userIdPhasePathId = mUI.getParams();


        Log.d(TAG, "Glass Tapped");
        Log.d(TAG, lastUserIdPhasePathId[0]);
        Log.d(TAG, lastUserIdPhasePathId[1]);
        Log.d(TAG, lastUserIdPhasePathId[2]);
        Log.d(TAG, "Glass Tapped");
        Log.d(TAG, userIdPhasePathId[0]);
        Log.d(TAG, userIdPhasePathId[1]);
        Log.d(TAG, userIdPhasePathId[2]);
        time = System.currentTimeMillis();
//        try {
//            TimeUnit.MILLISECONDS.sleep(300);
//        } catch (InterruptedException ex) {
//            throw new RuntimeException(ex);
//        }
        //format = sdf.format(new Date());
        if (time - lasttime > 3000) { //use 3s cooling down period because joycon has delay

            lasttime = time;
            userIdPhasePathId = mUI.getParams();

            if (bookOrderInPath < 100) {

                String bookTag = mCommHandler.getBookTag();
                mCommHandler.postToApi(userIdPhasePathId[0], userIdPhasePathId[1], Long.toString(time), userIdPhasePathId[2], bookTag);
                bookOrderInPath += 1;
                int res = mCommHandler.getBook(Integer.valueOf(userIdPhasePathId[2]), bookOrderInPath, context);
                if (res == 0) {
                    mUI.backToMenu();
                    bookOrderInPath = 100;
                } else {

                    PickRoute nextRoute = mCommHandler.getNextPickRoute();
//        if(nextRoute == null)
//            mUI.backToMenu();

                    mUI.setRoute(nextRoute);
                    mRenderer.setRoute(nextRoute);
                }
            } else if (userIdPhasePathId[2] != "") {

                bookOrderInPath = 0;
                int res = mCommHandler.getBook(Integer.valueOf(userIdPhasePathId[2]), bookOrderInPath, context);
                if (res == 0) {
                    bookOrderInPath = 100;

                } else {
                    mCommHandler.postToApi(userIdPhasePathId[0], userIdPhasePathId[1], Long.toString(time), userIdPhasePathId[2], "start of pickpath");
                    PickRoute nextRoute = mCommHandler.getNextPickRoute();
//        if(nextRoute == null)
//            mUI.backToMenu();

                    mUI.setRoute(nextRoute);
                    mRenderer.setRoute(nextRoute);
                }
            }
        }

    }

//            if (userIdPhasePathId[0] !="" &&userIdPhasePathId[1] !="" &&userIdPhasePathId[2] !="" && userIdPhasePathId[0] == lastUserIdPhasePathId[0] && userIdPhasePathId[1] == lastUserIdPhasePathId[1] && userIdPhasePathId[2] == lastUserIdPhasePathId[2]) {
//                String bookTag = mCommHandler.getBookTag();
//                mCommHandler.postToApi(userIdPhasePathId[0], userIdPhasePathId[1], Long.toString(time), userIdPhasePathId[2], bookTag);
//                bookOrderInPath += 1;
//                int res = mCommHandler.getBook(Integer.valueOf(userIdPhasePathId[2]), bookOrderInPath, context);
////                if ( res== 0){
////                    mUI.backToMenu();
////
////                }
//            } else if (userIdPhasePathId[0] == lastUserIdPhasePathId[0] && userIdPhasePathId[1] == lastUserIdPhasePathId[1] && userIdPhasePathId[2] != "") {
//                {
//
//                    mCommHandler.postToApi(userIdPhasePathId[0], userIdPhasePathId[1], Long.toString(time), userIdPhasePathId[2], "start of pickpath");
//                }
//                bookOrderInPath = 0;
//                int res = mCommHandler.getBook(Integer.valueOf(userIdPhasePathId[2]), bookOrderInPath, context);
////                if (res== 0){
////                    mUI.backToMenu();
////
////                }
//
//            }


//        PickRoute nextRoute = mCommHandler.getNextPickRoute();
////        if(nextRoute == null)
////            mUI.backToMenu();
//
//        mUI.setRoute(nextRoute);
//        mRenderer.setRoute(nextRoute);}


    //System.arraycopy(userIdPhasePathId,0, lastUserIdPhasePathId,0, 3);
//        Log.d(TAG, "mimi");
//        Log.d(TAG, lastUserIdPhasePathId[0]);
//        Log.d(TAG, "mimi");
//        Log.d(TAG, lastUserIdPhasePathId[1]);
//        Log.d(TAG, "mimi");
//       Log.d(TAG, lastUserIdPhasePathId[2]);
//        Log.d(TAG, "momo");
//        Log.d(TAG, userIdPhasePathId[0]);
//        Log.d(TAG, "momo");
//        Log.d(TAG, userIdPhasePathId[1]);
//        Log.d(TAG, "momo");
//       Log.d(TAG, userIdPhasePathId[2]);


    public void tap() {
        mUI.tap();
    }
    public void swipeLeft(){
        mUI.swipeLeft();
    }
    public void swipeRight(){
        mUI.swipeRight();
    }


    public void addUserView(){
        mUI.addUserView();
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
