package com.thad.locationtracker;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;


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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Prefs.SCREEN_HEIGHT = displayMetrics.heightPixels;
        Prefs.SCREEN_WIDTH = displayMetrics.widthPixels;

        mClient = new AndroidClient(this);
    }

    @Override
    protected void onDestroy(){
        mClient.onDestroy();
        super.onDestroy();
    }

}