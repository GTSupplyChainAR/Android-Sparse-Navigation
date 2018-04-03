package com.thad.locationtracker;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.thad.sparse_nav_lib.Static.Prefs;


/**
 * MobileMainActivity is the Main Activity of the mobile application.
 * Responsible for Activity Lifecycle.
 * Keep the code here minimal.
 */

public class MainActivity extends Activity {
    private static final String TAG = "|MainActivity|";

    private AndroidClient mClient;

    //Function called in the start of the application
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hides Title Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        int statusBarHeight = 0;
        int resource = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resource > 0)
            statusBarHeight = getResources().getDimensionPixelSize(resource);
        Log.d(TAG, "Status Bar: "+statusBarHeight);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Prefs.SCREEN_HEIGHT = displayMetrics.heightPixels - statusBarHeight;
        Prefs.SCREEN_WIDTH = displayMetrics.widthPixels;

        mClient = new AndroidClient(this);
    }

    public void changeBackground(View v){
        mClient.changeBackground();
    }

    @Override
    protected void onDestroy(){
        mClient.onDestroy();
        super.onDestroy();
    }

}