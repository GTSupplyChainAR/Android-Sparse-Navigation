package com.thad.sparsenavigation.UI;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thad.sparse_nav_lib.Static.Prefs;
import com.thad.sparse_nav_lib.UI.WarehouseMapView;
import com.thad.sparse_nav_lib.WarehouseLocation;
import com.thad.sparse_nav_lib.WarehouseMap;
import com.thad.sparsenavigation.R;

/**
 * Created by theo on 3/25/18.
 */

public class UserInterfaceHandler {
    private static final String TAG = "|UserInterfaceHandler|";

    private Activity activity;
    private WarehouseMapView mMapView;

    private float currentHeading;

    //UI Elements
    private TextView headingTitleView;
    private ImageView compassView;
    private PositionIndicator positionIndicator;

    public UserInterfaceHandler(Activity activity){
        this.activity = activity;
        init();
    }

    private void init(){
        currentHeading = 0f;

        mMapView = new WarehouseMapView(activity);
        //mMapView.setSize(Prefs.SCREEN_WIDTH, Prefs.SCREEN_HEIGHT);

        positionIndicator = new PositionIndicator(activity);

        RelativeLayout layout = (RelativeLayout) activity.findViewById(R.id.main_layout);
        layout.addView(mMapView);
        layout.addView(positionIndicator);

        //headingTitleView = (TextView) activity.findViewById(R.id.heading_title);
        //compassView = (ImageView) activity.findViewById(R.id.compass);
    }

    public void setMap(WarehouseMap map){
        mMapView.setMap(map);
        mMapView.generateUI();
    }

    public void onHeadingChanged(float heading){
        //updateCompassView(heading);
        positionIndicator.setHeading(heading);
    }
    public void onLocationUpdate(WarehouseLocation loc) {
        mMapView.setLocation(loc);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int[] last_loc = mMapView.getRawLocation();
                positionIndicator.setLocation(last_loc[0], last_loc[1]+20);
                //positionIndicator.invalidate();
            }
        });
    }

    private void updateCompassView(float heading){
        headingTitleView.setText("Heading: " + Float.toString(heading) + " degrees");

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentHeading, -heading,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        compassView.startAnimation(ra);
        currentHeading = -heading;
    }

}
