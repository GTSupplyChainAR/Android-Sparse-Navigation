package com.thad.sparsenavigation;

import android.app.Activity;
import android.os.Bundle;

/**
 * MobileMainActivity is the Main Activity of the mobile application.
 * Responsible for Activity Lifecycle.
 * Keep the code here minimal.
 */

public class MobileMainActivity extends Activity {
    private final static String TAG = "|MobileMainActivity|";

    private AndroidClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_main);

        mClient = new AndroidClient(this);
    }

    @Override
    protected void onDestroy(){
        mClient.onDestroy();
        super.onDestroy();
    }
}
