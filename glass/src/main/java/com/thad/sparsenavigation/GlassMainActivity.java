package com.thad.sparsenavigation;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.thad.sparse_nav_lib.Static.Prefs;

/**
 *
 */
public class GlassMainActivity extends Activity {
    private final static String TAG = "|GlassMainActivity|";

    private GlassClient mClient;

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
        if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {
            return true;
        }

        return super.onKeyDown(keycode, event);
    }

}
