package com.thad.sparse_nav_lib;

import android.util.Log;

import com.thad.sparse_nav_lib.Static.Prefs;

/**
 * Created by theo on 2/20/18.
 */

public class WarehouseMap {
    private static final String TAG = "|WarehouseMap|";

    private int grid_rows, grid_cols;
    private double ratio;
    private boolean[][] isObstacle;

    public WarehouseMap(){this(Prefs.MAP_WIDTH,Prefs.MAP_HEIGHT);}
    public WarehouseMap(int w, int h){
        grid_rows = h;
        grid_cols = w;
        ratio = ((double)w)/h;

        isObstacle = new boolean[h][w];
        for(int i = 0 ; i < h ; i++)
            for(int j = 0 ; j < w ; j++)
                isObstacle[i][j] = false;

    }


    public int[] getGridDims(){return new int[]{grid_rows, grid_cols};}
    public double getRatio(){return ratio;}
    public boolean isObstacle(int r, int c){
        if(r >= isObstacle.length || c >= isObstacle[0].length
                || r < 0 || c < 0)
            return true;
        return isObstacle[r][c];
    }


}
