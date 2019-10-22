package com.thad.sparsenavigation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

    private boolean shows3D = true, emulator = false;
    private int last_x = 0;

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

        View main_layout = findViewById(R.id.main_layout);
        mClient.addUserView();
//        main_layout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                int x = (int)event.getX();
//                int y = (int)event.getY();
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        Log.d(TAG, "Touch Down");
//                        last_x = x;
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        float normalizedX = (((float)(x-last_x))/Prefs.SCREEN_WIDTH - 0.5f)*2;
//                        Log.d(TAG, "Touch Move -> heading "+normalizedX);
//                        mClient.onSensorUpdate(normalizedX*360f, true);
//                        last_x = x;
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        Log.d(TAG, "Touch Up");
//                        int offset = 200;
//                        if(x < offset && y < offset) {
//                            Log.d(TAG, "Switch Views");
//                            if (shows3D)
//                                mClient.addVerticalShelfView();
//                            else
//                                mClient.deleteVerticalShelfView();
//                            shows3D = !shows3D;
//                        } else if (x > Prefs.SCREEN_WIDTH - offset && y < offset) {
//                            Log.d(TAG, "Get Next Pick Path.");
//                            mClient.glassTapped();
//                        }
//                        break;
//                }
//                Log.d(TAG, "x,y = "+x+','+y);
//                return false;
//            }
//        });
    }

    public void onMainClick(View v){}

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        Log.d(TAG,Integer.toString(keycode));
        Log.d(TAG, "KeyDown");
//        if (keycode == 96) { //96 is A on joycon
//           Log.d(TAG,"Glass Tapped 96");
//            // if tapped, update firebase to make one item is picked
//            mClient.glassTapped();
//            //return true;
//        }
//        else if (keycode == 98) {//98 is B on joycon
//            Log.d(TAG,"Glass Tapped 98");
//            // if tapped, update firebase to make one item is picked
//            mClient.switchView();
//            //return true;
//        }
        mClient.switchView();


        return super.onKeyDown(keycode, event);
    }

    private GestureDetector createGestureDetector(final Context context){
        try {
            Class.forName( "com.google.android.glass.touchpad.GestureDetector" );
            emulator = false;
        } catch( ClassNotFoundException e ) {
            Log.e(TAG, "GDK not found. You are probably running this on an emulator.");
            emulator = true;
            mClient.pause();
            return null;
        }

        GestureDetector gestureDetector = new GestureDetector(context);
        //Create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {

                if (gesture == Gesture.SWIPE_RIGHT) {
                    // do something on right (forward) swipe
                    Log.d(TAG,"Swiped Right");
                    mClient.swipeRight();
                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    // do something on left (backwards) swipe
                    Log.d(TAG,"Swiped Left");
                    mClient.swipeLeft();
                    return true;
                } else if (gesture == Gesture.TAP) {
                    Log.d(TAG, "Tapped");
                    mClient.tap();
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
