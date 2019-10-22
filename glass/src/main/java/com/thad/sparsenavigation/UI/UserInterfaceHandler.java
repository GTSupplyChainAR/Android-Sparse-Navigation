package com.thad.sparsenavigation.UI;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thad.sparse_nav_lib.PickRoute;
import com.thad.sparse_nav_lib.UI.WarehouseMapView;
import com.thad.sparse_nav_lib.WarehouseMap;
import com.thad.sparsenavigation.GlassClient;
import com.thad.sparsenavigation.Graphics.GLRenderer;
import com.thad.sparsenavigation.Graphics.GraphicsGLView;
import com.thad.sparsenavigation.R;

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
    private TextView selectHintView;
    private String[] selectValues = new String[] {"","1", "2", "3", "4", "5", "6",
            "7", "8", "9", "10", "11", "12"};
    private String[] userIdValues = new String[] {"","1", "2", "3", "4", "5", "6",
            "7", "8", "9", "10", "11", "12"};
    private String[] phaseValues = new String[] {"", "training", "testing"};
    private String[] pathValues;
    private ListView userSelectListView;
    private ListView phaseSelectListView;
    private ListView pathSelectListView;

    private String[] userIdPhasePathId = new String[] {"","",""};
    private String userId;
    private String phase;
    private String pathId;

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
        //usView = new UserSelectView(getContext());
        //layout.addView(usView);



//        selectHintView = new TextView(getContext());
//        applyTextStyle(selectHintView);
//        selectHintView.setText("Please select User ID");
//        layout.addView(selectHintView);
//
//
        userSelectListView = new ListView(getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, selectValues);
        userSelectListView.setAdapter(adapter);
        layout.addView(userSelectListView);
//
//        phaseSelectListView = new ListView(getContext());
//        phaseSelectListView.setVisibility(View.GONE);
//        phaseSelectListView.setAdapter(adapter);
//        layout.addView(phaseSelectListView);
//
//        pathSelectListView = new ListView(getContext());
//        pathSelectListView.setVisibility(View.GONE);
//        pathSelectListView.setAdapter(adapter);
//        layout.addView(pathSelectListView);

        aiView = new AisleView(getContext());
        layout.addView(aiView);
        aiView.setVisibility(View.GONE);

        vsView = new VerticalShelfView(getContext());
        layout.addView(vsView);
        vsView.setVisibility(View.GONE);

        phView = new PhaseSelectView(getContext());
        layout.addView(phView);
        phView.setVisibility(View.GONE);

        usView = new UserSelectView(getContext());
        layout.addView(usView);
        usView.setVisibility(View.GONE);

        paView = new PathSelectView(getContext());
        layout.addView(paView);
        paView.setVisibility(View.GONE);


        usView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "HERE");
            }
        });

//        userSelectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // selected item
//                userId = ((TextView) view).getText().toString();
//                userIdPhasePathId[0] = userId;
//                Toast.makeText(getContext(), "Selected User :" + userId, Toast.LENGTH_SHORT).show();
//                userSelectListView.setVisibility(View.GONE);
//                selectHintView.setText("Please select phase");
//                selectValues = new String[] {"", "1", "2"};
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
//                        android.R.layout.simple_list_item_1, selectValues);
//                phaseSelectListView.setAdapter(adapter);
//                phaseSelectListView.setVisibility(View.VISIBLE);
//
//
//            }
//        });
//
//        phaseSelectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // selected item
//                phase= ((TextView) view).getText().toString();
//                userIdPhasePathId[1] = phase;
//                Toast.makeText(getContext(), "Selected phase :" +phase, Toast.LENGTH_SHORT).show();
//                userSelectListView.setVisibility(View.GONE);
//                phaseSelectListView.setVisibility(View.GONE);
//                selectHintView.setText("Please select pathID");
//                if (phase == "training"){selectValues = new String[] {"","1", "2", "3", "4", "5", "6",
//                        "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17","18","19","20"};}
//                        else {selectValues = new String[] {"","21","22","23","24","25","26","27","28",
//                "29","30","31","32","33","34","35","36","37","38","39","40"};}
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
//                        android.R.layout.simple_list_item_1, selectValues);
//                pathSelectListView.setAdapter(adapter);
//                pathSelectListView.setVisibility(View.VISIBLE);
//
//            }
//        });
//
//        pathSelectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // selected item
//                pathId = ((TextView) view).getText().toString();
//                userIdPhasePathId[2] = pathId;
//                Toast.makeText(getContext(), "Selected path :" +pathId, Toast.LENGTH_SHORT).show();
//                userSelectListView.setVisibility(View.GONE);
//                phaseSelectListView.setVisibility(View.GONE);
//                pathSelectListView.setVisibility(View.GONE);
//                selectHintView.setVisibility(View.GONE);
//                selectHintView.setText("Press Trigger for Next Book, Bumper for Change View");
//                selectValues = new String[] {"","1", "2", "3", "4", "5", "6",
//                        "7", "8", "9", "10", "11", "12"};
//                //userSelectListView.setVisibility(View.VISIBLE);
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
//                        android.R.layout.simple_list_item_1, selectValues);
//                userSelectListView.setAdapter(adapter);
//                aiView.setVisibility(View.VISIBLE);
//
//            }
//        });

        //Log.d(TAG, product);

    }

    public String[] getParams(){
        Log.d(TAG, userIdPhasePathId[0]);
        return userIdPhasePathId;
    }

    public void backToMenu(){
        aiView.setVisibility(View.GONE);
        vsView.setVisibility(View.GONE);
        userSelectListView.setVisibility(View.VISIBLE);
        selectHintView.setVisibility(View.VISIBLE);
        Log.d(TAG,"Menu");
    }
    public void addUserView() {
        usView.setVisibility(View.VISIBLE);
    }
    public void tap() {
        if(usView.getVisibility() == View.VISIBLE) {
            usView.setVisibility(View.GONE);
            phView.setVisibility(View.VISIBLE);
        } else if (phView.getVisibility() == View.VISIBLE) {
            phView.setVisibility(View.GONE);
            paView.setVisibility(View.VISIBLE);
        } else if (paView.getVisibility() == View.VISIBLE) {
            paView.setVisibility(View.GONE);
            vsView.setVisibility(View.VISIBLE);
        }
    }
    public void switchView(){

    }

    public void addVerticalShelfView(){
        if (vsView.getVisibility() == View.VISIBLE) {
            vsView.setVisibility(View.GONE);
            aiView.setVisibility(View.VISIBLE);
        }
       Log.d(TAG,"Swipe Left");
    }

    public void deleteVerticalShelfView() {
        if (aiView.getVisibility() == View.VISIBLE) {
            aiView.setVisibility(View.GONE);
            vsView.setVisibility(View.VISIBLE);
        }
        Log.d(TAG, "Swipe Right");
    }

    public void setMap(WarehouseMap map){
        mMapView.setMap(map);
        mMapView.generateUI();
        Log.d(TAG, "set map");
    }

    public void setRoute(PickRoute newRoute){
        if(newRoute == null)
            return;
        vsView.setTargetBook(newRoute.getTargetBook());
        aiView.setTargetBook(newRoute.getTargetBook());
        Log.d(TAG, "set route");

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
    private void applyTextStyle(TextView textView){
        textView.setTextColor(Color.rgb(255,255,255));
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);

    }

}
