package com.thad.sparse_nav_lib.UI;

import android.content.Context;
import android.preference.PreferenceScreen;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.thad.sparse_nav_lib.R;
import com.thad.sparse_nav_lib.Utils.Vec;
import com.thad.sparse_nav_lib.WarehouseLocation;
import com.thad.sparse_nav_lib.WarehouseMap;


/**
 * This Class holds the UI elements that represent our Map
 * Any code here runs on the UI thread, so keep the code here minimal.
 * All the other UI elements and controls should be on the UserInterface Handler instead.
 */

public class WarehouseMapView extends LinearLayout {
    private static final String TAG = "|MapView|";

    private static final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    private static final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    //All in pixels
    private int SCREEN_WIDTH, SCREEN_HEIGHT;
    private int width, height, map_rows, map_cols;
    float cell_width, cell_height;

    private Context mContext;
    private WarehouseMap mMap;

    private WarehouseLocation lastLocation;


    public WarehouseMapView(Context context){
        super(context);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        lastLocation = new WarehouseLocation();
    }


    public boolean generateUI(){
        if(mMap == null) return false;

        //This function generates the Map UI
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        this.setLayoutParams(layoutParams);
        this.setOrientation(VERTICAL);

        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, 1f);

        for(int i = 0 ; i < map_rows ; i++){
            LinearLayout ll_row = new LinearLayout(mContext);
            ll_row.setLayoutParams(lp);
            ll_row.setOrientation(HORIZONTAL);

            for(int j = 0 ; j < map_cols ; j++){
                ImageView cell = new ImageView(mContext);
                cell.setLayoutParams(lp);
                cell.setImageResource(R.drawable.grid_cell);
                cell.setTag(i+","+j);

                //Here you could also set an OnClick Listener to every cell.
                //That way we know which cell the user taps (recognize through Tag)


                ll_row.addView(cell);
            }
            this.addView(ll_row);
        }
        // END OF SAMPLE CODE

        return true;
    }

    public void setLocationFromViewCoords(int x, int y){
        float[] normalized = new float[]{((float)x)/width, ((float)y)/height};

        int r = (int)(normalized[1]*mMap.getGridDims()[0]);
        int c = (int)(normalized[0]*mMap.getGridDims()[1]);
        lastLocation.setCell(r,c);

        Vec locationFromTopLeft = new Vec(x,y);
        Vec cellFromTopLeft = new Vec(cell_width*(c+0.5), cell_height*(r+0.5));
        Vec displacement = locationFromTopLeft.sub(cellFromTopLeft);
        displacement.x = 2 * displacement.x / cell_width;
        displacement.y = -2 * displacement.y / cell_height;
        lastLocation.setDisplacement(displacement);
    }

    public void setMap(WarehouseMap map){
        mMap = map;
        map_rows = mMap.getGridDims()[0];
        map_cols = mMap.getGridDims()[1];

        double ratio = mMap.getRatio();
        width = (ratio < 1)?(int)(SCREEN_HEIGHT*ratio):SCREEN_WIDTH;
        height = (ratio < 1)?SCREEN_HEIGHT:(int)(SCREEN_WIDTH/ratio);

        cell_width = ((float)width)/map_cols;
        cell_height = ((float)height)/map_rows;
    }

    public void setSize(int width, int height){SCREEN_WIDTH = width; SCREEN_HEIGHT = height;}

    public WarehouseLocation getLocation(){
        return lastLocation;
    }
    public int[] getRawLocation(){
        Vec canvasToView = new Vec(0, (SCREEN_HEIGHT-height)/2);
        int[] pos = lastLocation.getCell();
        Vec vCell = new Vec(cell_width*(pos[1]+0.5), cell_height*(pos[0]+0.5));
        Vec displacement = lastLocation.getDisplacement();
        displacement.x = displacement.x * cell_width / 2;
        displacement.y = -displacement.y * cell_height / 2;

        Vec canvasPos = canvasToView.add(vCell.add(displacement));
        return new int[]{(int)canvasPos.x, (int)canvasPos.y};
    }
}
