package com.thad.sparsenavigation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.thad.sparse_nav_lib.Static.Prefs;

/**
 *
 */
public class GlassMainActivity extends Activity {
    private final static String TAG = "|GlassMainActivity|";

    private GlassClient mClient;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.navigation_layout);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Prefs.SCREEN_HEIGHT = displayMetrics.heightPixels;
        Prefs.SCREEN_WIDTH = displayMetrics.widthPixels;

        mClient = new GlassClient(this);
        mGestureDetector = createGestureDetector(this);

    }

    @Override
    protected void onResume(){
        mClient.resume();
        super.onResume();
    }

    @Override
    protected void onPause(){
        mClient.pause();
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        Log.d(TAG,"ouchouchouch");
        if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {
           Log.d(TAG,"tapped, tapped, tapped");
            // if tapped, update firebase to make one item is picked
            mClient.confirmPicked();
            return true;
        }

        return super.onKeyDown(keycode, event);
    }

    private GestureDetector createGestureDetector(Context context){
        try {
            Class.forName( "com.google.android.glass.touchpad.GestureDetector" );
        } catch( ClassNotFoundException e ) {
            Log.e(TAG, "GDK not found.");
            return null;
        }
        GestureDetector gestureDetector = new GestureDetector(context);
        //Create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if (gesture == Gesture.SWIPE_RIGHT) {
                    // do something on right (forward) swipe
                    Log.d(TAG,"oops, in");
                    mClient.deleteVerticalShelfView();
                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    // do something on left (backwards) swipe
                    Log.d(TAG,"oops, out");
                    mClient.addVerticalShelfView();
                    return true;
                }
                return false;
            }

        });
        return gestureDetector;

    }

    /*
    * Send generic motion events to the gesture detector
    */
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return false;
    }
}
