package com.thad.sparsenavigation.UI;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thad.sparse_nav_lib.PickRoute;
import com.thad.sparse_nav_lib.Static.Prefs;
import com.thad.sparse_nav_lib.UI.WarehouseMapView;
import com.thad.sparse_nav_lib.WarehouseLocation;
import com.thad.sparse_nav_lib.WarehouseMap;
import com.thad.sparsenavigation.GlassClient;
import com.thad.sparsenavigation.Graphics.GLRenderer;
import com.thad.sparsenavigation.Graphics.GraphicsGLView;
import com.thad.sparsenavigation.R;
import com.thad.sparsenavigation.UI.VerticalShelfView;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by theo on 3/25/18.
 */

public class UserInterfaceHandler {
    private static final String TAG = "|UserInterfaceHandler|";

    private Activity activity;
    private GlassClient mClient;

    private WarehouseMapView mMapView;
    private GraphicsGLView glView;
    private AisleView aiView;
    private VerticalShelfView vsView;

    private UserSelectView usView;
    private PhaseSelectView phView;
    private PathSelectView paView;


    private float currentHeading;

    //UI Elements
    private TextView headingTitleView;
    private ImageView compassView;
    private PositionIndicator positionIndicator;

    public UserInterfaceHandler(GlassClient client){
        this.activity = (Activity)client.getContext();
        mClient = client;
        init();
    }

    private void init(){
        currentHeading = 0f;

        mMapView = new WarehouseMapView(activity);
        //mMapView.setSize(Prefs.SCREEN_WIDTH, Prefs.SCREEN_HEIGHT);

        positionIndicator = new PositionIndicator(activity);

        RelativeLayout layout = (RelativeLayout) activity.findViewById(R.id.main_layout);
        //layout.addView(mMapView);
        //layout.addView(positionIndicator);

        //headingTitleView = (TextView) activity.findViewById(R.id.heading_title);
        //compassView = (ImageView) activity.findViewById(R.id.compass);

        //LinearLayout container = activity.findViewById(R.id.graphics_container);

        //glView = new GraphicsGLView(this);
        //layout.addView(glView);
        usView = new UserSelectView(getContext());
        layout.addView(usView);

        aiView = new AisleView(getContext());
        layout.addView(aiView);
        aiView.setVisibility(View.GONE);

        vsView = new VerticalShelfView(getContext());
        layout.addView(vsView);
        vsView.setVisibility(View.GONE);
        //Log.d(TAG, product);

    }

    public void addVerticalShelfView(){
        aiView.setVisibility(View.GONE);
        vsView.setVisibility(View.VISIBLE);
       // Log.d(TAG,"visible now hahahah");
    }

    public void deleteVerticalShelfView() {
        vsView.setVisibility(View.GONE);
        aiView.setVisibility(View.VISIBLE);
        // Log.d(TAG, "invisible meow");
    }

    public void setMap(WarehouseMap map){
        mMapView.setMap(map);
        mMapView.generateUI();
    }

    public void setRoute(PickRoute newRoute){
        if(newRoute == null)
            return;
        vsView.setTargetBook(newRoute.getTargetBook());
        aiView.setTargetBook(newRoute.getTargetBook());
    }

    public void onHeadingChanged(float heading, boolean isFake){
        //updateCompassView(heading);
        positionIndicator.setHeading(heading);
        if(isFake)
            glView.addHeading(heading);
        else
            glView.setHeading(heading);
    }


    public Context getContext(){return activity;}
    public GLRenderer getRenderer(){return mClient.getRenderer(); }


    /*
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
    */

}
