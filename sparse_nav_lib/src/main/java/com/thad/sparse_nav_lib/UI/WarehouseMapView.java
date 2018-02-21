package com.thad.sparse_nav_lib.UI;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.thad.sparse_nav_lib.R;
import com.thad.sparse_nav_lib.WarehouseLocation;
import com.thad.sparse_nav_lib.WarehouseMap;


/**
 * This Class holds the UI elements that represent our Map
 * Any code here runs on the UI thread, so keep the code here minimal.
 * All the other UI elements and controls should be on the UserInterface Handler instead.
 */

public class WarehouseMapView extends LinearLayout {
    private static final String TAG = "|MapView|";

    private Context mContext;
    private WarehouseMap mMap;


    public WarehouseMapView(Context context){
        super(context);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        this.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.setOrientation(VERTICAL);
    }


    public boolean generateUI(){
        //This function generates the Map UI



        //SAMPLE CODE  TO CREATE 2D GRID - REPLACE WITH YOUR CODE
        if(mMap == null) return false;

        int h = mMap.getGridDims()[0];
        int w = mMap.getGridDims()[1];

        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, 1f);

        for(int i = 0 ; i < h ; i++){
            LinearLayout ll_row = new LinearLayout(mContext);
            ll_row.setLayoutParams(lp);
            ll_row.setOrientation(HORIZONTAL);

            for(int j = 0 ; j < w ; j++){
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


    public void setMap(WarehouseMap map){ mMap = map; }

    public WarehouseLocation getLocation(){
        return new WarehouseLocation();
    }
}
