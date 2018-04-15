package com.thad.sparse_nav_lib.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.thad.sparse_nav_lib.R;
import com.thad.sparse_nav_lib.Static.Prefs;
import com.thad.sparse_nav_lib.Utils.Vec;
import com.thad.sparse_nav_lib.WarehouseLocation;
import com.thad.sparse_nav_lib.WarehouseMap;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;


/**
 * This Class holds the UI elements that represent our Map
 * Any code here runs on the UI thread, so keep the code here minimal.
 * All the other UI elements and controls should be on the UserInterface Handler instead.
 */

public class WarehouseMapView extends RelativeLayout {
    private static final String TAG = "|MapView|";

    private static final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    private static final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    //All in pixels
    private float width, height, cell_width, cell_height;
    private int map_rows, map_cols;


    private Context mContext;
    private WarehouseMap mMap;

    private boolean isRotated;

    private WarehouseLocation lastLocation;
    private ImageView warehouseBackgroundImg;
    private boolean show_grid = false;

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

        this.removeAllViews();
        //This function generates the Map UI
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)width, (int)height);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        this.setLayoutParams(layoutParams);

        LinearLayout grid = new LinearLayout(mContext);
        if(show_grid) {
            grid.setLayoutParams(new LayoutParams(MP, MP));
            grid.setOrientation(VERTICAL);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MP, MP, 1f);

            for (int i = 0; i < map_rows; i++) {
                LinearLayout ll_row = new LinearLayout(mContext);
                ll_row.setLayoutParams(lp);
                ll_row.setOrientation(HORIZONTAL);

                for (int j = 0; j < map_cols; j++) {
                    ImageView cell = new ImageView(mContext);
                    cell.setLayoutParams(lp);
                    cell.setImageResource(R.drawable.grid_cell);
                    cell.setTag(i + "," + j);

                    ll_row.addView(cell);
                }
                grid.addView(ll_row);
            }
        }

        Drawable warehouseDrawable = mContext.getResources().getDrawable(R.drawable.map_realistic);
        if(isRotated)
            warehouseDrawable = mContext.getResources().getDrawable(R.drawable.map_realistic_glass);

        warehouseBackgroundImg = new ImageView(mContext);
        warehouseBackgroundImg.setLayoutParams(new LayoutParams(MP, MP));
        warehouseBackgroundImg.setBackground(warehouseDrawable);

        this.addView(warehouseBackgroundImg);
        if(show_grid)
            this.addView(grid);

        return true;
    }

    public void setLocation(WarehouseLocation loc){
        lastLocation = new WarehouseLocation(loc);
        if(isRotated){
            lastLocation.setCell(map_rows-loc.getCell()[1]-1, loc.getCell()[0]);
            Vec rotatedV = new Vec(-loc.getDisplacement().y, loc.getDisplacement().x);
            lastLocation.setDisplacement(rotatedV);
        }
    }

    public void setLocationFromViewCoords(int x, int y){

        if(x < 0){
            setLocationFromViewCoords(0, y);
            return;
        } else if (x >= width){
            setLocationFromViewCoords((int)width-1, y);
            return;
        }
        if(y < 0){
            setLocationFromViewCoords(x, 0);
            return;
        }else if (y >= height){
            setLocationFromViewCoords(x, (int)height-1);
            return;
        }


        float[] normalized = new float[]{x/width, y/height};

        int r = (int)(normalized[1]*map_rows);
        int c = (int)(normalized[0]*map_cols);

        boolean isValid = !mMap.isObstacle(r,c);

        if(isValid)
            lastLocation.setCell(r,c);

        Vec locationFromTopLeft = new Vec(x,y);
        Vec cellFromTopLeft = new Vec(cell_width*(c+0.5), cell_height*(r+0.5));
        Vec displacement = locationFromTopLeft.sub(cellFromTopLeft);
        displacement.x = 2 * displacement.x / cell_width;
        displacement.y = -2 * displacement.y / cell_height;


        //Find the closest position if cursor is over an obstacle.
        if(!isValid) {
            Log.d(TAG, "Cursor is on obstacle. Finding closest valid cell.");
            Vec ref = new Vec(cell_width*(c+0.5), cell_height*(r+0.5)).add(
                    new Vec( displacement.x * cell_width / 2, -displacement.y * cell_height / 2));

            Vec[] dir = new Vec[4];
            dir[0] = new Vec(-1,0);
            dir[1] = new Vec(1,0);
            dir[2] = new Vec(0,-1);
            dir[3] = new Vec(0,1);

            float min_d = Float.MAX_VALUE;
            int min_ind = -1;
            int[] closestCell = new int[2];
            for(int i = 0 ; i < 4 ; i++) {
                int cur_r = r, cur_c = c;
                float dist = -1;
                while (cur_r >= 0 && cur_c >= 0 &&
                        cur_r < map_rows && cur_c < map_cols) {
                    cur_c += dir[i].x;
                    cur_r -= dir[i].y;
                    if (!mMap.isObstacle(cur_r, cur_c)) {
                        dist = ref.distance(new Vec(cell_width*(cur_c+0.5),
                                        cell_height*(cur_r+0.5)));
                        break;
                    }
                }
                if (dist == -1) {
                    continue;
                } else if (dist < min_d){
                    min_d = dist;
                    min_ind = i;
                    closestCell = new int[]{cur_r, cur_c};
                }
            }
            if(min_ind == -1) {
                return;
            }

            lastLocation.setCell(closestCell[0], closestCell[1]);

            Vec n_displacement = new Vec(displacement);
            if(dir[min_ind].x != 0)
                n_displacement.x = -dir[min_ind].x;
            else if (dir[min_ind].y != 0)
                n_displacement.y = -dir[min_ind].y;

            lastLocation.setDisplacement(n_displacement);
        }else
            lastLocation.setDisplacement(displacement);
    }

    public void setMap(WarehouseMap map){
        mMap = map;

        double ratio = mMap.getRatio();
        double screen_ratio = ((double)Prefs.SCREEN_WIDTH)/Prefs.SCREEN_HEIGHT;


        if((ratio-1)*(screen_ratio-1)<0){
            isRotated = true;

            map_rows = mMap.getGridDims()[1];
            map_cols = mMap.getGridDims()[0];

            width = (ratio < 1)? Prefs.SCREEN_WIDTH :(float) (Prefs.SCREEN_HEIGHT / ratio);
            height = (ratio < 1)? (float)(Prefs.SCREEN_WIDTH * ratio): Prefs.SCREEN_HEIGHT;

        }else{
            isRotated = false;

            map_rows = mMap.getGridDims()[0];
            map_cols = mMap.getGridDims()[1];

            width = (ratio < 1)? (float) (Prefs.SCREEN_HEIGHT * ratio) :Prefs.SCREEN_WIDTH;
            height = (ratio < 1)?Prefs.SCREEN_HEIGHT: (float) (Prefs.SCREEN_WIDTH / ratio);
        }

        cell_width = width/map_cols;
        cell_height = height/map_rows;
    }

    public WarehouseLocation getLocation(){
        return lastLocation;
    }
    public int[] getRawLocation(){
        Vec canvasToView = new Vec(0, (Prefs.SCREEN_HEIGHT-height)/2);
        if(mMap.getRatio() < 1 || isRotated)
            canvasToView = new Vec((Prefs.SCREEN_WIDTH-width)/2, 0);


        int[] pos = lastLocation.getCell();
        Vec vCell = new Vec(cell_width*(pos[1]+0.5), cell_height*(pos[0]+0.5));
        Vec displacement = new Vec(lastLocation.getDisplacement());
        displacement.x = displacement.x * cell_width / 2;
        displacement.y = -displacement.y * cell_height / 2;

        Vec canvasPos = canvasToView.add(vCell.add(displacement));
        return new int[]{(int)canvasPos.x, (int)canvasPos.y};
    }

    public void changeBackground() {
        show_grid = !show_grid;
        generateUI();
    }
}
